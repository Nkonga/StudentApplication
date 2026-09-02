package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private StudentRepository repository;
    private Button btnBackHome;
    private TextView tvStudentCount;
    private EditText etSearch;
    private List<Student> allStudents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvStudentCount = findViewById(R.id.tvStudentCount);
        etSearch = findViewById(R.id.etSearch);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up search functionality
        setupSearch();

        repository = StudentRepository.getInstance(this);
        loadStudents();
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    // Show all students
                    updateAdapter(allStudents);
                } else {
                    // Search students
                    searchStudents(query);
                }
            }
        });
    }

    private void searchStudents(String query) {
        repository.searchStudents(query, new StudentRepository.StudentsCallback() {
            @Override
            public void onResult(List<Student> students) {
                updateAdapter(students);
                if (students.isEmpty()) {
                    Toast.makeText(StudentListActivity.this,
                            "No students found matching: " + query,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentListActivity.this,
                        "Search error: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        repository.getStudents(new StudentRepository.StudentsCallback() {
            @Override
            public void onResult(List<Student> students) {
                allStudents = students;
                updateAdapter(students);
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentListActivity.this,
                        "Error loading students: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdapter(List<Student> students) {
        if (students.isEmpty()) {
            tvStudentCount.setText("Total Students: 0");
        } else {
            tvStudentCount.setText("Total Students: " + students.size());
        }
        adapter = new StudentAdapter(students);
        adapter.setOnStudentClickListener(new StudentAdapter.OnStudentClickListener() {
            @Override
            public void onStudentClick(Student student) {
                Intent intent = new Intent(StudentListActivity.this, StudentDetailsActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}