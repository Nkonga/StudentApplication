package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView tvFullName, tvStudentId, tvGrade, tvGender, tvDOB, tvPhone;
    private ImageView ivStudentProfile;
    private Button btnBack, btnEdit, btnDelete;

    private Student student;
    private StudentRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle system/status bar
        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        getWindow().setStatusBarColor(Color.BLACK);

        setContentView(R.layout.activity_student_details);

        // Apply status bar inset to the whole root layout
        View studentDetailsRoot =
                findViewById(R.id.studentDetailsRoot);

        ViewCompat.setOnApplyWindowInsetsListener(
                studentDetailsRoot,
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

        // Find views
        btnBack = findViewById(R.id.btnBack);
        ivStudentProfile = findViewById(R.id.ivStudentProfile);

        tvFullName = findViewById(R.id.tvFullName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvGrade = findViewById(R.id.tvGrade);
        tvGender = findViewById(R.id.tvGender);
        tvDOB = findViewById(R.id.tvDOB);
        tvPhone = findViewById(R.id.tvPhone);

        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Repository
        repository = StudentRepository.getInstance(this);

        // Get student passed from StudentListActivity
        student = (Student) getIntent().getSerializableExtra("student");

        // Check student data
        if (student == null) {
            Toast.makeText(
                    this,
                    "No student data found!",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        // Display student
        displayStudentDetails();

        // Set up buttons
        setupButtons();
    }

    private void displayStudentDetails() {

        tvFullName.setText(student.getFullName());

        tvStudentId.setText("ID: " + student.getStudentId());

        tvGrade.setText("Grade: " + student.getGrade());

        tvGender.setText("Gender: " + student.getGender());

        tvDOB.setText("Date of Birth: " + student.getDateOfBirth());

        tvPhone.setText("Phone: " + student.getPhone());

        // Display profile image if available
        String imagePath = student.getProfileImagePath();

        if (imagePath != null && !imagePath.isEmpty()) {

            File imageFile = new File(imagePath);

            if (imageFile.exists()) {

                Glide.with(this)
                        .load(imageFile)
                        .circleCrop()
                        .placeholder(R.drawable.ic_default_profile)
                        .error(R.drawable.ic_default_profile)
                        .into(ivStudentProfile);

            } else {

                ivStudentProfile.setImageResource(
                        R.drawable.ic_default_profile
                );
            }

        } else {

            ivStudentProfile.setImageResource(
                    R.drawable.ic_default_profile
            );
        }
    }

    private void setupButtons() {

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Edit button
        btnEdit.setOnClickListener(v -> showEditConfirmation());

        // Delete button
        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void showEditConfirmation() {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Edit")
                .setMessage(
                        "Are you sure you want to edit the record of:\n\n" +
                                student.getFullName() + "\n" +
                                student.getStudentId()
                )
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(
                        "Yes, Edit",
                        (dialog, which) -> editStudent()
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmation() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage(
                        "Are you sure you want to permanently delete:\n\n" +
                                student.getFullName() + "\n" +
                                student.getStudentId() +
                                "\n\nThis action cannot be undone!"
                )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        "Yes, Delete",
                        (dialog, which) -> deleteStudent()
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void editStudent() {

        Intent intent = new Intent(
                StudentDetailsActivity.this,
                StudentRegistrationActivity.class
        );

        intent.putExtra("edit_mode", true);
        intent.putExtra("student", student);

        startActivity(intent);
        finish();
    }

    private void deleteStudent() {

        repository.deleteStudent(
                student,
                new StudentRepository.SaveCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(
                                StudentDetailsActivity.this,
                                "Student deleted successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    }

                    @Override
                    public void onError(Exception exception) {

                        Toast.makeText(
                                StudentDetailsActivity.this,
                                "Error: " + exception.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
