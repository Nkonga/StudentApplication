package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private StudentRepository repository;
    private TextView tvStudentCount;
    private EditText etSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Student> allStudents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Setup Material Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvStudentCount = findViewById(R.id.tvStudentCount);
        etSearch = findViewById(R.id.etSearch);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Setup SwipeRefreshLayout
        setupSwipeRefresh();
        setupSearch();

        repository = StudentRepository.getInstance(this);
        loadStudents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Back navigation (Home button)
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        // Refresh action
        if (id == R.id.action_refresh) {
            swipeRefreshLayout.setRefreshing(true);
            loadStudents();
            Toast.makeText(this, "🔄 Refreshing...", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Search action - Focus on search bar
        if (id == R.id.action_search) {
            etSearch.requestFocus();
            etSearch.performClick();
            return true;
        }

        // Export action
        if (id == R.id.action_export) {
            Toast.makeText(this, "📁 Export feature coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Settings
        if (id == R.id.action_settings) {
            Toast.makeText(this, "⚙️ Settings coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Help
        if (id == R.id.action_help) {
            Toast.makeText(this, "❓ Help coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        }

        // About
        if (id == R.id.action_about) {
            Toast.makeText(this, "ℹ️ About Student App v1.0", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        swipeRefreshLayout.setOnRefreshListener(() -> loadStudents());
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    updateAdapter(allStudents);
                } else {
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
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentListActivity.this,
                        "Error loading students: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void updateAdapter(List<Student> students) {
        tvStudentCount.setText("Total: " + students.size());
        adapter = new StudentAdapter(students);
        adapter.setOnStudentClickListener(student -> {
            Intent intent = new Intent(StudentListActivity.this, StudentDetailsActivity.class);
            intent.putExtra("student", student);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}