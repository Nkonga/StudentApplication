package com.nkongamoses.studentapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailsActivity extends AppCompatActivity {
    private TextView tvFullName, tvStudentId, tvGrade, tvGender, tvDOB, tvPhone;
    private Button btnEdit, btnDelete, btnBack;
    private Student student;
    private StudentRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        // Initialize views
        tvFullName = findViewById(R.id.tvFullName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvGrade = findViewById(R.id.tvGrade);
        tvGender = findViewById(R.id.tvGender);
        tvDOB = findViewById(R.id.tvDOB);
        tvPhone = findViewById(R.id.tvPhone);

        // IMPORTANT: These are the buttons!
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        // Debug toast to confirm activity is opening
        Toast.makeText(this, "Details Activity Opened!", Toast.LENGTH_SHORT).show();

        repository = StudentRepository.getInstance(this);

        // Get student from intent
        student = (Student) getIntent().getSerializableExtra("student");

        if (student == null) {
            Toast.makeText(this, "No student data found!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        displayStudentDetails();
        setupButtons();
    }

    private void displayStudentDetails() {
        tvFullName.setText(student.getFullName());
        tvStudentId.setText("ID: " + student.getStudentId());
        tvGrade.setText("Grade: " + student.getGrade());
        tvGender.setText("Gender: " + student.getGender());
        tvDOB.setText("Date of Birth: " + student.getDateOfBirth());
        tvPhone.setText("Phone: " + student.getPhone());
    }

    private void setupButtons() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Edit button - with confirmation
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Edit button clicked!", Toast.LENGTH_SHORT).show();
            showEditConfirmation();
        });

        // Delete button - with confirmation
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Delete button clicked!", Toast.LENGTH_SHORT).show();
            showDeleteConfirmation();
        });
    }

    private void showEditConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("✏️ Confirm Edit")
                .setMessage("Are you sure you want to edit the record of:\n\n" +
                        "📌 " + student.getFullName() + "\n" +
                        "🆔 " + student.getStudentId())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Yes, Edit", (dialog, which) -> editStudent())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("⚠️ Delete Student")
                .setMessage("Are you sure you want to permanently delete:\n\n" +
                        "📌 " + student.getFullName() + "\n" +
                        "🆔 " + student.getStudentId() + "\n\n" +
                        "This action cannot be undone!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes, Delete", (dialog, which) -> deleteStudent())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void editStudent() {
        Intent intent = new Intent(this, StudentRegistrationActivity.class);
        intent.putExtra("edit_mode", true);
        intent.putExtra("student", student);
        startActivity(intent);
        finish();
    }

    private void deleteStudent() {
        repository.deleteStudent(student, new StudentRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentDetailsActivity.this,
                        "🗑️ Student deleted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentDetailsActivity.this,
                        "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}