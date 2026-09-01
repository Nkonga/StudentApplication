package com.nkongamoses.studentapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentRegistrationActivity extends AppCompatActivity {

    private EditText etStudentId;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etGrade;
    private EditText etGender;
    private EditText etDateOfBirth;
    private EditText etPhone;

    private Button btnSaveStudent;
    private Button btnBackHome;

    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        studentRepository = StudentRepository.getInstance();

        etStudentId = findViewById(R.id.etStudentId);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etGrade = findViewById(R.id.etGrade);
        etGender = findViewById(R.id.etGender);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etPhone = findViewById(R.id.etPhone);

        btnSaveStudent = findViewById(R.id.btnSaveStudent);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnSaveStudent.setOnClickListener(v -> saveStudent());

        btnBackHome.setOnClickListener(v -> finish());
    }

    private void saveStudent() {

        String studentId = etStudentId.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("Enter student ID");
            etStudentId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("Enter first name");
            etFirstName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError("Enter last name");
            etLastName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(grade)) {
            etGrade.setError("Enter grade or class");
            etGrade.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            etGender.setError("Enter gender");
            etGender.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(dateOfBirth)) {
            etDateOfBirth.setError("Enter date of birth");
            etDateOfBirth.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter phone number");
            etPhone.requestFocus();
            return;
        }

        Student student = new Student(
                studentId,
                firstName,
                lastName,
                grade,
                gender,
                dateOfBirth,
                phone
        );

        studentRepository.addStudent(student);

        Toast.makeText(
                this,
                "Student saved successfully",
                Toast.LENGTH_SHORT
        ).show();

        clearForm();
    }

    private void clearForm() {

        etStudentId.setText("");
        etFirstName.setText("");
        etLastName.setText("");
        etGrade.setText("");
        etGender.setText("");
        etDateOfBirth.setText("");
        etPhone.setText("");

        etStudentId.requestFocus();
    }
}