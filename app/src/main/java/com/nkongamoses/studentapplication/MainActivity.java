package com.nkongamoses.studentapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etStudentId;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etGrade;
    private EditText etGender;
    private EditText etDateOfBirth;
    private EditText etPhone;
    private Button btnSaveStudent;

    private RecyclerView recyclerStudents;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect form fields
        etStudentId = findViewById(R.id.etStudentId);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etGrade = findViewById(R.id.etGrade);
        etGender = findViewById(R.id.etGender);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etPhone = findViewById(R.id.etPhone);

        btnSaveStudent = findViewById(R.id.btnSaveStudent);

        // Connect RecyclerView
        recyclerStudents = findViewById(R.id.recyclerStudents);

        // Create student list
        studentList = new ArrayList<>();

        // Add temporary test students
        studentList.add(new Student(
                "ST001",
                "John",
                "Banda",
                "10A",
                "Male",
                "01/01/2012",
                "0970000000"
        ));

        studentList.add(new Student(
                "ST002",
                "Mary",
                "Phiri",
                "10B",
                "Female",
                "05/03/2012",
                "0960000000"
        ));

        // Set up RecyclerView
        studentAdapter = new StudentAdapter(studentList);

        recyclerStudents.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerStudents.setAdapter(studentAdapter);

        // Save button
        btnSaveStudent.setOnClickListener(v -> saveStudent());
    }

    private void saveStudent() {

        String studentId = etStudentId.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (studentId.isEmpty()) {
            etStudentId.setError("Enter student ID");
            etStudentId.requestFocus();
            return;
        }

        if (firstName.isEmpty()) {
            etFirstName.setError("Enter first name");
            etFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("Enter last name");
            etLastName.requestFocus();
            return;
        }

        if (grade.isEmpty()) {
            etGrade.setError("Enter grade or class");
            etGrade.requestFocus();
            return;
        }

        if (gender.isEmpty()) {
            etGender.setError("Enter gender");
            etGender.requestFocus();
            return;
        }

        if (dateOfBirth.isEmpty()) {
            etDateOfBirth.setError("Enter date of birth");
            etDateOfBirth.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Enter phone number");
            etPhone.requestFocus();
            return;
        }

        // Create a Student object
        Student student = new Student(
                studentId,
                firstName,
                lastName,
                grade,
                gender,
                dateOfBirth,
                phone
        );

        // Add student to the list
        studentList.add(student);

        // Tell RecyclerView that a new student was added
        studentAdapter.notifyItemInserted(studentList.size() - 1);

        Toast.makeText(
                this,
                "Student saved successfully",
                Toast.LENGTH_LONG
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