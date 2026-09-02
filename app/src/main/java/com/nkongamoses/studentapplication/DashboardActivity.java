package com.nkongamoses.studentapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalStudents, tvTotalGrades, tvMaleStudents, tvFemaleStudents;
    private TextView tvMalePercentage, tvFemalePercentage;
    private TextView tvGradeACount, tvGradeBCount, tvGradeCCount, tvGradeDCount, tvGradeFCount;
    private View viewMaleProgress, viewFemaleProgress;
    private View viewGradeA, viewGradeB, viewGradeC, viewGradeD, viewGradeF;
    private Button btnBackHome;
    private StudentRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvTotalGrades = findViewById(R.id.tvTotalGrades);
        tvMaleStudents = findViewById(R.id.tvMaleStudents);
        tvFemaleStudents = findViewById(R.id.tvFemaleStudents);
        tvMalePercentage = findViewById(R.id.tvMalePercentage);
        tvFemalePercentage = findViewById(R.id.tvFemalePercentage);
        viewMaleProgress = findViewById(R.id.viewMaleProgress);
        viewFemaleProgress = findViewById(R.id.viewFemaleProgress);
        tvGradeACount = findViewById(R.id.tvGradeACount);
        tvGradeBCount = findViewById(R.id.tvGradeBCount);
        tvGradeCCount = findViewById(R.id.tvGradeCCount);
        tvGradeDCount = findViewById(R.id.tvGradeDCount);
        tvGradeFCount = findViewById(R.id.tvGradeFCount);
        viewGradeA = findViewById(R.id.viewGradeA);
        viewGradeB = findViewById(R.id.viewGradeB);
        viewGradeC = findViewById(R.id.viewGradeC);
        viewGradeD = findViewById(R.id.viewGradeD);
        viewGradeF = findViewById(R.id.viewGradeF);
        btnBackHome = findViewById(R.id.btnBackHome);

        repository = StudentRepository.getInstance(this);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        repository.getStudents(new StudentRepository.StudentsCallback() {
            @Override
            public void onResult(List<Student> students) {
                updateDashboard(students);
            }

            @Override
            public void onError(Exception exception) {
                // Handle error - could show a toast
            }
        });
    }

    private void updateDashboard(List<Student> students) {
        int total = students.size();
        int maleCount = 0;
        int femaleCount = 0;
        int gradeACount = 0, gradeBCount = 0, gradeCCount = 0, gradeDCount = 0, gradeFCount = 0;

        // Count statistics
        for (Student student : students) {
            // Gender count
            String gender = student.getGender().toLowerCase();
            if (gender.equals("male")) {
                maleCount++;
            } else if (gender.equals("female")) {
                femaleCount++;
            }

            // Grade count
            String grade = student.getGrade().toUpperCase();
            if (grade.startsWith("A") || grade.equals("A+") || grade.equals("A-")) {
                gradeACount++;
            } else if (grade.startsWith("B") || grade.equals("B+") || grade.equals("B-")) {
                gradeBCount++;
            } else if (grade.startsWith("C") || grade.equals("C+") || grade.equals("C-")) {
                gradeCCount++;
            } else if (grade.startsWith("D") || grade.equals("D+") || grade.equals("D-")) {
                gradeDCount++;
            } else if (grade.startsWith("F")) {
                gradeFCount++;
            }
        }

        // Update Total Students
        tvTotalStudents.setText(String.valueOf(total));

        // Update Total Grades
        int totalGrades = gradeACount + gradeBCount + gradeCCount + gradeDCount + gradeFCount;
        tvTotalGrades.setText(String.valueOf(totalGrades));

        // Update Gender Counts
        tvMaleStudents.setText(String.valueOf(maleCount));
        tvFemaleStudents.setText(String.valueOf(femaleCount));

        // Update Gender Percentages
        if (total > 0) {
            int malePercent = (int) ((double) maleCount / total * 100);
            int femalePercent = (int) ((double) femaleCount / total * 100);

            tvMalePercentage.setText(malePercent + "%");
            tvFemalePercentage.setText(femalePercent + "%");

            // Update progress bars
            int maxWidth = 300;
            int maleWidth = (int) ((double) maleCount / total * maxWidth);
            int femaleWidth = (int) ((double) femaleCount / total * maxWidth);

            viewMaleProgress.getLayoutParams().width = Math.max(maleWidth, 10);
            viewFemaleProgress.getLayoutParams().width = Math.max(femaleWidth, 10);

            viewMaleProgress.requestLayout();
            viewFemaleProgress.requestLayout();
        } else {
            tvMalePercentage.setText("0%");
            tvFemalePercentage.setText("0%");
        }

        // Update Grade Counts
        tvGradeACount.setText(String.valueOf(gradeACount));
        tvGradeBCount.setText(String.valueOf(gradeBCount));
        tvGradeCCount.setText(String.valueOf(gradeCCount));
        tvGradeDCount.setText(String.valueOf(gradeDCount));
        tvGradeFCount.setText(String.valueOf(gradeFCount));

        // Update Grade Progress Bars
        int maxGrade = Math.max(Math.max(Math.max(Math.max(gradeACount, gradeBCount), gradeCCount), gradeDCount), gradeFCount);
        if (maxGrade > 0) {
            updateGradeProgress(viewGradeA, gradeACount, maxGrade);
            updateGradeProgress(viewGradeB, gradeBCount, maxGrade);
            updateGradeProgress(viewGradeC, gradeCCount, maxGrade);
            updateGradeProgress(viewGradeD, gradeDCount, maxGrade);
            updateGradeProgress(viewGradeF, gradeFCount, maxGrade);
        }
    }

    private void updateGradeProgress(View view, int count, int max) {
        int maxWidth = 300;
        int width = (int) ((double) count / max * maxWidth);
        view.getLayoutParams().width = Math.max(width, 10);
        view.requestLayout();
    }
}