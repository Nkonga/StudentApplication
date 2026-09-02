package com.nkongamoses.studentapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StudentRegistrationActivity extends AppCompatActivity {
    private EditText etStudentId, etFirstName, etLastName, etGrade, etGender, etDateOfBirth, etPhone;
    private Button btnSave;
    private StudentRepository repository;
    private boolean isEditMode = false;
    private Student editingStudent;

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

        repository = StudentRepository.getInstance(this);

        // Check if in edit mode
        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        if (isEditMode) {
            editingStudent = (Student) getIntent().getSerializableExtra("student");
            if (editingStudent != null) {
                populateFields(editingStudent);
                btnSave.setText("UPDATE STUDENT");
                setTitle("✏️ Edit Student");
                // Disable student ID editing (primary key)
                etStudentId.setEnabled(false);
                etStudentId.setAlpha(0.5f);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    // Validate and show confirmation before updating
                    if (validateFields()) {
                        showUpdateConfirmation();
                    }
                } else {
                    // Direct save for new student
                    saveStudent();
                }
            }
        });
    }

    private void populateFields(Student student) {
        etStudentId.setText(student.getStudentId());
        etFirstName.setText(student.getFirstName());
        etLastName.setText(student.getLastName());
        etGrade.setText(student.getGrade());
        etGender.setText(student.getGender());
        etDateOfBirth.setText(student.getDateOfBirth());
        etPhone.setText(student.getPhone());
    }

    /**
     * Validate all input fields
     */
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
            Toast.makeText(this, "⚠️ Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Show confirmation dialog before updating student
     */
    private void showUpdateConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("⚠️ Confirm Update")
                .setMessage("Are you sure you want to update the record of:\n\n" +
                        "📌 " + editingStudent.getFullName() + "\n" +
                        "🆔 " + editingStudent.getStudentId() + "\n\n" +
                        "Changes will be saved permanently.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("✅ Yes, Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStudent();
                    }
                })
                .setNegativeButton("❌ Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(StudentRegistrationActivity.this,
                                "❌ Update cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
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

        repository.addStudent(student, new StudentRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentRegistrationActivity.this,
                        "✅ Student saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentRegistrationActivity.this,
                        "❌ Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
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

        // Update student object
        editingStudent.setFirstName(firstName);
        editingStudent.setLastName(lastName);
        editingStudent.setGrade(grade);
        editingStudent.setGender(gender);
        editingStudent.setDateOfBirth(dateOfBirth);
        editingStudent.setPhone(phone);

        repository.updateStudent(editingStudent, new StudentRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(StudentRegistrationActivity.this,
                        "✅ Student updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentRegistrationActivity.this,
                        "❌ Error updating student: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}