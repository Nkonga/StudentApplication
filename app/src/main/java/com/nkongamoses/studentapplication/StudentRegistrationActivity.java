package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class StudentRegistrationActivity extends AppCompatActivity {
    private EditText etStudentId, etFirstName, etLastName, etGrade, etGender, etDateOfBirth, etPhone;
    private Button btnSave, btnSelectImage;
    private ImageView ivProfileImage;
    private StudentRepository repository;
    private boolean isEditMode = false;
    private Student editingStudent;
    private ScrollView scrollView;
    private String currentImagePath = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        // Initialize views
        etStudentId = findViewById(R.id.etStudentId);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etGrade = findViewById(R.id.etGrade);
        etGender = findViewById(R.id.etGender);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        scrollView = findViewById(R.id.scrollView);

        setupScrollOnFocus();

        repository = StudentRepository.getInstance(this);

        // Check if in edit mode
        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        if (isEditMode) {
            editingStudent = (Student) getIntent().getSerializableExtra("student");
            if (editingStudent != null) {
                populateFields(editingStudent);
                btnSave.setText("UPDATE STUDENT");
                setTitle("Edit Student");
                etStudentId.setEnabled(false);
                etStudentId.setAlpha(0.5f);
            }
        }

        // Select Image button click
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    if (validateFields()) {
                        showUpdateConfirmation();
                    }
                } else {
                    saveStudent();
                }
            }
        });
    }

    private void showImagePickerOptions() {
        String[] options = {"Take Photo", "Choose from Gallery", "Remove Photo"};

        new AlertDialog.Builder(this)
                .setTitle("Select Profile Photo")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Take Photo
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_REQUEST);
                    } else if (which == 1) {
                        // Choose from Gallery
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
                    } else if (which == 2) {
                        // Remove Photo
                        currentImagePath = null;
                        ivProfileImage.setImageResource(R.drawable.ic_default_profile);
                        Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                // Gallery image
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    saveImageAndDisplay(imageUri);
                }
            } else if (requestCode == CAMERA_REQUEST) {
                // Camera image
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    saveBitmapAndDisplay(bitmap);
                }
            }
        }
    }

    private void saveImageAndDisplay(Uri imageUri) {
        try {
            // Save image to cache
            String fileName = UUID.randomUUID().toString() + ".jpg";
            File cacheFile = new File(getCacheDir(), fileName);

            // Copy image to cache
            java.io.InputStream inputStream = getContentResolver().openInputStream(imageUri);
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(cacheFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            currentImagePath = cacheFile.getAbsolutePath();

            // Display image
            Glide.with(this)
                    .load(cacheFile)
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_profile)
                    .into(ivProfileImage);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmapAndDisplay(Bitmap bitmap) {
        try {
            // Save bitmap to cache
            String fileName = UUID.randomUUID().toString() + ".jpg";
            File cacheFile = new File(getCacheDir(), fileName);

            FileOutputStream outputStream = new FileOutputStream(cacheFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

            currentImagePath = cacheFile.getAbsolutePath();

            // Display image
            Glide.with(this)
                    .load(cacheFile)
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_profile)
                    .into(ivProfileImage);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupScrollOnFocus() {
        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, v.getTop() - 50);
                        }
                    }, 100);
                }
            }
        };

        etStudentId.setOnFocusChangeListener(focusListener);
        etFirstName.setOnFocusChangeListener(focusListener);
        etLastName.setOnFocusChangeListener(focusListener);
        etGrade.setOnFocusChangeListener(focusListener);
        etGender.setOnFocusChangeListener(focusListener);
        etDateOfBirth.setOnFocusChangeListener(focusListener);
        etPhone.setOnFocusChangeListener(focusListener);
    }

    private void populateFields(Student student) {
        etStudentId.setText(student.getStudentId());
        etFirstName.setText(student.getFirstName());
        etLastName.setText(student.getLastName());
        etGrade.setText(student.getGrade());
        etGender.setText(student.getGender());
        etDateOfBirth.setText(student.getDateOfBirth());
        etPhone.setText(student.getPhone());

        // Load profile image if exists
        if (student.getProfileImagePath() != null && !student.getProfileImagePath().isEmpty()) {
            currentImagePath = student.getProfileImagePath();
            Glide.with(this)
                    .load(new File(student.getProfileImagePath()))
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_profile)
                    .into(ivProfileImage);
        }
    }

    private boolean validateFields() {
        String studentId = etStudentId.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                grade.isEmpty() || gender.isEmpty() || dateOfBirth.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showUpdateConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Are you sure you want to update " + editingStudent.getFullName() + "'s information?")
                .setPositiveButton("Yes, Update", (dialog, which) -> updateStudent())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveStudent() {
        if (!validateFields()) {
            return;
        }

        String studentId = etStudentId.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        Student student = new Student(studentId, firstName, lastName, grade, gender, dateOfBirth, phone);
        student.setProfileImagePath(currentImagePath);

        repository.addStudent(student, new StudentRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentRegistrationActivity.this,
                        "Student saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentRegistrationActivity.this,
                        "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStudent() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        editingStudent.setFirstName(firstName);
        editingStudent.setLastName(lastName);
        editingStudent.setGrade(grade);
        editingStudent.setGender(gender);
        editingStudent.setDateOfBirth(dateOfBirth);
        editingStudent.setPhone(phone);
        editingStudent.setProfileImagePath(currentImagePath);

        repository.updateStudent(editingStudent, new StudentRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentRegistrationActivity.this,
                        "Student updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentRegistrationActivity.this,
                        "Error updating student: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}