package com.nkongamoses.studentapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class StudentRegistrationActivity extends AppCompatActivity {

    private EditText etStudentId, etFirstName, etLastName, etGrade,
            etGender, etDateOfBirth, etPhone;

    private Button btnSave;
    private Button btnSelectImage;

    private ImageView ivProfileImage;
    private ImageView ivClose;

    private StudentRepository repository;

    private boolean isEditMode = false;
    private Student editingStudent;

    private String currentImagePath = null;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Enable edge-to-edge so that we can handle
         * the status bar consistently across screens.
         */
        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        /*
         * Keep the status bar black.
         */
        getWindow().setStatusBarColor(Color.BLACK);

        setContentView(R.layout.activity_student_registration);

        /*
         * Get the entire screen root.
         */
        View registrationRoot =
                findViewById(R.id.registrationRoot);

        /*
         * Apply the status-bar inset to the ENTIRE root.
         *
         * Do NOT apply it directly to the Toolbar.
         * This keeps the toolbar below the black status bar.
         */
        ViewCompat.setOnApplyWindowInsetsListener(
                registrationRoot,
                (view, windowInsets) -> {

                    Insets systemBars =
                            windowInsets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    view.setPadding(
                            0,
                            systemBars.top,
                            0,
                            0
                    );

                    return windowInsets;
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar()
                        .setDisplayHomeAsUpEnabled(true);

                getSupportActionBar()
                        .setDisplayShowTitleEnabled(false);
            }
        }

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
        ivClose = findViewById(R.id.ivClose);

        repository = StudentRepository.getInstance(this);

        isEditMode =
                getIntent().getBooleanExtra(
                        "edit_mode",
                        false
                );

        if (isEditMode) {

            editingStudent =
                    (Student) getIntent()
                            .getSerializableExtra("student");

            if (editingStudent != null) {

                populateFields(editingStudent);

                btnSave.setText("UPDATE");

                setTitle("Edit Student");

                etStudentId.setEnabled(false);
                etStudentId.setAlpha(0.5f);
            }
        }

        ivClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        StudentRegistrationActivity.this,
                                        MainActivity.class
                                );

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        );

                        startActivity(intent);
                        finish();
                    }
                }
        );

        btnSelectImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermissionAndShowPicker();
                    }
                }
        );

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isEditMode) {
                            updateStudent();
                        } else {
                            saveStudent();
                        }
                    }
                }
        );
    }

    private void checkPermissionAndShowPicker() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        },
                        PERMISSION_REQUEST_CODE
                );

                return;
            }
        }

        showImagePickerOptions();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                showImagePickerOptions();

            } else {

                Toast.makeText(
                        this,
                        "Permission denied to read storage",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void showImagePickerOptions() {

        String[] options = {
                "Take Photo",
                "Choose from Gallery",
                "Remove Photo"
        };

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Profile Photo")
                .setItems(
                        options,
                        (dialog, which) -> {

                            if (which == 0) {

                                Intent intent =
                                        new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE
                                        );

                                startActivityForResult(
                                        intent,
                                        CAMERA_REQUEST
                                );

                            } else if (which == 1) {

                                Intent intent =
                                        new Intent(
                                                Intent.ACTION_PICK,
                                                MediaStore.Images.Media
                                                        .EXTERNAL_CONTENT_URI
                                        );

                                startActivityForResult(
                                        intent,
                                        PICK_IMAGE_REQUEST
                                );

                            } else if (which == 2) {

                                currentImagePath = null;

                                ivProfileImage.setImageResource(
                                        R.drawable.ic_default_profile
                                );

                                Toast.makeText(
                                        this,
                                        "Photo removed",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == PICK_IMAGE_REQUEST) {

                Uri imageUri = data.getData();

                if (imageUri != null) {
                    saveImageAndDisplay(imageUri);
                }

            } else if (requestCode == CAMERA_REQUEST) {

                if (data.getExtras() != null) {

                    Bitmap bitmap =
                            (Bitmap) data.getExtras()
                                    .get("data");

                    if (bitmap != null) {
                        saveBitmapAndDisplay(bitmap);
                    }
                }
            }
        }
    }

    private void saveImageAndDisplay(Uri imageUri) {

        try {

            String fileName =
                    UUID.randomUUID().toString()
                            + ".jpg";

            File cacheFile =
                    new File(
                            getCacheDir(),
                            fileName
                    );

            java.io.InputStream inputStream =
                    getContentResolver()
                            .openInputStream(imageUri);

            FileOutputStream outputStream =
                    new FileOutputStream(cacheFile);

            byte[] buffer = new byte[1024];

            int length;

            while ((length =
                    inputStream.read(buffer)) > 0) {

                outputStream.write(
                        buffer,
                        0,
                        length
                );
            }

            outputStream.close();
            inputStream.close();

            currentImagePath =
                    cacheFile.getAbsolutePath();

            Glide.with(this)
                    .load(cacheFile)
                    .circleCrop()
                    .placeholder(
                            R.drawable.ic_default_profile
                    )
                    .into(ivProfileImage);

        } catch (IOException e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Error saving image",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void saveBitmapAndDisplay(Bitmap bitmap) {

        try {

            String fileName =
                    UUID.randomUUID().toString()
                            + ".jpg";

            File cacheFile =
                    new File(
                            getCacheDir(),
                            fileName
                    );

            FileOutputStream outputStream =
                    new FileOutputStream(cacheFile);

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    80,
                    outputStream
            );

            outputStream.close();

            currentImagePath =
                    cacheFile.getAbsolutePath();

            Glide.with(this)
                    .load(cacheFile)
                    .circleCrop()
                    .placeholder(
                            R.drawable.ic_default_profile
                    )
                    .into(ivProfileImage);

        } catch (IOException e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Error saving image",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void populateFields(Student student) {

        etStudentId.setText(
                student.getStudentId()
        );

        etFirstName.setText(
                student.getFirstName()
        );

        etLastName.setText(
                student.getLastName()
        );

        etGrade.setText(
                student.getGrade()
        );

        etGender.setText(
                student.getGender()
        );

        etDateOfBirth.setText(
                student.getDateOfBirth()
        );

        etPhone.setText(
                student.getPhone()
        );

        if (student.getProfileImagePath() != null
                && !student.getProfileImagePath().isEmpty()) {

            currentImagePath =
                    student.getProfileImagePath();

            Glide.with(this)
                    .load(
                            new File(
                                    student.getProfileImagePath()
                            )
                    )
                    .circleCrop()
                    .placeholder(
                            R.drawable.ic_default_profile
                    )
                    .into(ivProfileImage);
        }
    }

    private void saveStudent() {

        String studentId =
                etStudentId.getText()
                        .toString()
                        .trim();

        String firstName =
                etFirstName.getText()
                        .toString()
                        .trim();

        String lastName =
                etLastName.getText()
                        .toString()
                        .trim();

        String grade =
                etGrade.getText()
                        .toString()
                        .trim();

        String gender =
                etGender.getText()
                        .toString()
                        .trim();

        String dateOfBirth =
                etDateOfBirth.getText()
                        .toString()
                        .trim();

        String phone =
                etPhone.getText()
                        .toString()
                        .trim();

        if (studentId.isEmpty()
                || firstName.isEmpty()
                || lastName.isEmpty()
                || grade.isEmpty()
                || gender.isEmpty()
                || dateOfBirth.isEmpty()
                || phone.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Student student =
                new Student(
                        studentId,
                        firstName,
                        lastName,
                        grade,
                        gender,
                        dateOfBirth,
                        phone
                );

        student.setProfileImagePath(
                currentImagePath
        );

        repository.addStudent(
                student,
                new StudentRepository.SaveCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(
                                StudentRegistrationActivity.this,
                                "Student saved successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    }

                    @Override
                    public void onError(
                            Exception exception) {

                        Toast.makeText(
                                StudentRegistrationActivity.this,
                                "Error: "
                                        + exception.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void updateStudent() {

        String firstName =
                etFirstName.getText()
                        .toString()
                        .trim();

        String lastName =
                etLastName.getText()
                        .toString()
                        .trim();

        String grade =
                etGrade.getText()
                        .toString()
                        .trim();

        String gender =
                etGender.getText()
                        .toString()
                        .trim();

        String dateOfBirth =
                etDateOfBirth.getText()
                        .toString()
                        .trim();

        String phone =
                etPhone.getText()
                        .toString()
                        .trim();

        if (firstName.isEmpty()
                || lastName.isEmpty()
                || grade.isEmpty()
                || gender.isEmpty()
                || dateOfBirth.isEmpty()
                || phone.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        editingStudent.setFirstName(firstName);
        editingStudent.setLastName(lastName);
        editingStudent.setGrade(grade);
        editingStudent.setGender(gender);
        editingStudent.setDateOfBirth(dateOfBirth);
        editingStudent.setPhone(phone);
        editingStudent.setProfileImagePath(
                currentImagePath
        );

        repository.updateStudent(
                editingStudent,
                new StudentRepository.SaveCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(
                                StudentRegistrationActivity.this,
                                "Student updated successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    }

                    @Override
                    public void onError(
                            Exception exception) {

                        Toast.makeText(
                                StudentRegistrationActivity.this,
                                "Error updating student: "
                                        + exception.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item) {

        if (item.getItemId()
                == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}