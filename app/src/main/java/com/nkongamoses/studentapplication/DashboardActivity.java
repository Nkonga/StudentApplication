package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalStudents, tvTotalGrades, tvMaleStudents, tvFemaleStudents;
    private TextView tvMalePercentage, tvFemalePercentage;
    private TextView tvGradeACount, tvGradeBCount, tvGradeCCount, tvGradeDCount, tvGradeFCount;
    private View viewMaleProgress, viewFemaleProgress;
    private View viewGradeA, viewGradeB, viewGradeC, viewGradeD, viewGradeF;
    private StudentRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle system/status bar
        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        getWindow().setStatusBarColor(Color.BLACK);

        setContentView(R.layout.activity_dashboard);

        // Apply status bar inset to the whole root layout
        View dashboardRoot =
                findViewById(R.id.dashboardRoot);

        ViewCompat.setOnApplyWindowInsetsListener(
                dashboardRoot,
                (view, windowInsets) -> {

                    Insets systemBars =
                            windowInsets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    view.setPadding(
                            0,
                            systemBars.top,
                            0,
                            0
                    );

                    return windowInsets;
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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

        Button btnBackHome =
                findViewById(R.id.btnBackHome);

        repository =
                StudentRepository.getInstance(this);

        btnBackHome.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
            finish();
        });

        loadDashboardData();
    }

    private void loadDashboardData() {

        repository.getStudents(
                new StudentRepository.StudentsCallback() {

                    @Override
                    public void onResult(
                            List<Student> students) {

                        updateDashboard(students);
                    }

                    @Override
                    public void onError(
                            Exception exception) {
                        // Handle error
                    }
                }
        );
    }

    private void updateDashboard(
            List<Student> students) {

        int total = students.size();

        int maleCount = 0;
        int femaleCount = 0;

        int gradeACount = 0;
        int gradeBCount = 0;
        int gradeCCount = 0;
        int gradeDCount = 0;
        int gradeFCount = 0;

        for (Student student : students) {

            String gender =
                    student.getGender().toLowerCase();

            if (gender.equals("male")) {
                maleCount++;
            } else if (gender.equals("female")) {
                femaleCount++;
            }

            String grade =
                    student.getGrade().toUpperCase();

            if (grade.startsWith("A")
                    || grade.equals("A+")
                    || grade.equals("A-")) {

                gradeACount++;

            } else if (grade.startsWith("B")
                    || grade.equals("B+")
                    || grade.equals("B-")) {

                gradeBCount++;

            } else if (grade.startsWith("C")
                    || grade.equals("C+")
                    || grade.equals("C-")) {

                gradeCCount++;

            } else if (grade.startsWith("D")
                    || grade.equals("D+")
                    || grade.equals("D-")) {

                gradeDCount++;

            } else if (grade.startsWith("F")) {

                gradeFCount++;
            }
        }

        tvTotalStudents.setText(
                String.valueOf(total)
        );

        int totalGrades =
                gradeACount
                        + gradeBCount
                        + gradeCCount
                        + gradeDCount
                        + gradeFCount;

        tvTotalGrades.setText(
                String.valueOf(totalGrades)
        );

        tvMaleStudents.setText(
                String.valueOf(maleCount)
        );

        tvFemaleStudents.setText(
                String.valueOf(femaleCount)
        );

        if (total > 0) {

            int malePercent =
                    (int) (
                            (double) maleCount
                                    / total
                                    * 100
                    );

            int femalePercent =
                    (int) (
                            (double) femaleCount
                                    / total
                                    * 100
                    );

            tvMalePercentage.setText(
                    malePercent + "%"
            );

            tvFemalePercentage.setText(
                    femalePercent + "%"
            );

            int maxWidth = 300;

            int maleWidth =
                    (int) (
                            (double) maleCount
                                    / total
                                    * maxWidth
                    );

            int femaleWidth =
                    (int) (
                            (double) femaleCount
                                    / total
                                    * maxWidth
                    );

            viewMaleProgress
                    .getLayoutParams()
                    .width =
                    Math.max(maleWidth, 10);

            viewFemaleProgress
                    .getLayoutParams()
                    .width =
                    Math.max(femaleWidth, 10);

            viewMaleProgress.requestLayout();
            viewFemaleProgress.requestLayout();

        } else {

            tvMalePercentage.setText("0%");
            tvFemalePercentage.setText("0%");
        }

        tvGradeACount.setText(
                String.valueOf(gradeACount)
        );

        tvGradeBCount.setText(
                String.valueOf(gradeBCount)
        );

        tvGradeCCount.setText(
                String.valueOf(gradeCCount)
        );

        tvGradeDCount.setText(
                String.valueOf(gradeDCount)
        );

        tvGradeFCount.setText(
                String.valueOf(gradeFCount)
        );

        int maxGrade =
                Math.max(
                        Math.max(
                                Math.max(
                                        Math.max(
                                                gradeACount,
                                                gradeBCount
                                        ),
                                        gradeCCount
                                ),
                                gradeDCount
                        ),
                        gradeFCount
                );

        if (maxGrade > 0) {

            updateGradeProgress(
                    viewGradeA,
                    gradeACount,
                    maxGrade
            );

            updateGradeProgress(
                    viewGradeB,
                    gradeBCount,
                    maxGrade
            );

            updateGradeProgress(
                    viewGradeC,
                    gradeCCount,
                    maxGrade
            );

            updateGradeProgress(
                    viewGradeD,
                    gradeDCount,
                    maxGrade
            );

            updateGradeProgress(
                    viewGradeF,
                    gradeFCount,
                    maxGrade
            );
        }
    }

    private void updateGradeProgress(
            View view,
            int count,
            int max) {

        int maxWidth = 300;

        int width =
                (int) (
                        (double) count
                                / max
                                * maxWidth
                );

        view.getLayoutParams().width =
                Math.max(width, 10);

        view.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item) {

        if (item.getItemId()
                == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}