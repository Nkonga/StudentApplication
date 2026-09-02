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
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        repository = StudentRepository.getInstance(this);

        // Get student from intent - using Serializable
        student = (Student) getIntent().getSerializableExtra("student");

        if (student != null) {
            displayStudentDetails();
        }

        // Edit button click
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editStudent();
            }
        });

        // Delete button click
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });

        // Back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayStudentDetails() {
        tvFullName.setText(student.getFullName());
        tvStudentId.setText("ID: " + student.getStudentId());
        tvGrade.setText("Grade: " + student.getGrade());
        tvGender.setText("Gender: " + student.getGender());
        tvDOB.setText("Date of Birth: " + student.getDateOfBirth());
        tvPhone.setText("Phone: " + student.getPhone());
    }

    private void editStudent() {
        Intent intent = new Intent(StudentDetailsActivity.this, StudentRegistrationActivity.class);
        intent.putExtra("edit_mode", true);
        intent.putExtra("student", student);
        startActivity(intent);
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete " + student.getFullName() + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStudent();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteStudent() {
        repository.deleteStudent(student, new StudentRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentDetailsActivity.this,
                        "Student deleted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentDetailsActivity.this,
                        "Error deleting student: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}