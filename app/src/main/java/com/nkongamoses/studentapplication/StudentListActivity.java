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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private StudentRepository repository;
    private Button btnBackHome;
    private TextView tvStudentCount;
    private EditText etSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Setup SwipeRefreshLayout
        setupSwipeRefresh();

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

    private void setupSwipeRefresh() {
        // Set colors for the refresh indicator
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        // Set refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data
                loadStudents();
            }
        });
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
                // Stop refresh if it's still showing
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentListActivity.this,
                        "Search error: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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
                // Stop the refresh animation
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                // Show toast with refresh count
                Toast.makeText(StudentListActivity.this,
                        "✅ Refreshed! " + students.size() + " students found",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentListActivity.this,
                        "Error loading students: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
                // Stop the refresh animation
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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