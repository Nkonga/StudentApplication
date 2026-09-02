package com.nkongamoses.studentapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private StudentRepository repository;
    private Button btnBackHome;
    private TextView tvStudentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add divider between items
        // Uncomment if you want simple dividers instead of CardView
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        tvStudentCount = findViewById(R.id.tvStudentCount);
        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        repository = StudentRepository.getInstance(this);
        loadStudents();
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
                if (students.isEmpty()) {
                    Toast.makeText(StudentListActivity.this,
                            "No students found. Please register a student.",
                            Toast.LENGTH_SHORT).show();
                    tvStudentCount.setText("Total Students: 0");
                } else {
                    tvStudentCount.setText("Total Students: " + students.size());
                }
                adapter = new StudentAdapter(students);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(StudentListActivity.this,
                        "Error loading students: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}