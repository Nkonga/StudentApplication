package com.nkongamoses.studentapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private Button btnBackHome;

    private StudentAdapter studentAdapter;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        btnBackHome = findViewById(R.id.btnBackHome);

        studentRepository = StudentRepository.getInstance();

        List<Student> students = studentRepository.getStudents();

        studentAdapter = new StudentAdapter(students);

        recyclerViewStudents.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewStudents.setAdapter(studentAdapter);

        btnBackHome.setOnClickListener(v -> finish());
    }
}