package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private static final String TAG = "StudentList";

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private StudentRepository repository;
    private TextView tvStudentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        getWindow().setStatusBarColor(Color.BLACK);

        setContentView(R.layout.activity_student_list);

        Log.d(TAG, "onCreate: Started");

        /*
         * Apply status-bar inset to the entire screen.
         * Do NOT apply it directly to the Toolbar.
         */
        android.view.View studentListRoot =
                findViewById(R.id.studentListRoot);

        ViewCompat.setOnApplyWindowInsetsListener(
                studentListRoot,
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

        if (toolbar != null) {

            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {

                getSupportActionBar()
                        .setDisplayHomeAsUpEnabled(true);

                getSupportActionBar()
                        .setDisplayShowTitleEnabled(false);
            }

        } else {

            Log.e(
                    TAG,
                    "Toolbar not found in activity_student_list.xml"
            );
        }

        recyclerView =
                findViewById(R.id.recyclerViewStudents);

        if (recyclerView == null) {

            Log.e(
                    TAG,
                    "RecyclerView not found!"
            );

            Toast.makeText(
                    this,
                    "RecyclerView not found!",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        tvStudentCount =
                findViewById(R.id.tvStudentCount);

        if (tvStudentCount == null) {

            Log.e(
                    TAG,
                    "tvStudentCount not found!"
            );

            Toast.makeText(
                    this,
                    "Student count TextView not found!",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        repository =
                StudentRepository.getInstance(this);

        Log.d(
                TAG,
                "Repository instance: " + repository
        );

        if (repository == null) {

            Toast.makeText(
                    this,
                    "Repository is null!",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        loadStudents();
    }

    @Override
    protected void onResume() {

        super.onResume();

        Log.d(
                TAG,
                "onResume: Reloading students"
        );

        if (repository != null) {
            loadStudents();
        }
    }

    private void loadStudents() {

        Log.d(
                TAG,
                "loadStudents: Called"
        );

        Toast.makeText(
                this,
                "Loading students...",
                Toast.LENGTH_SHORT
        ).show();

        if (repository == null) {

            Log.e(
                    TAG,
                    "Repository is null!"
            );

            return;
        }

        repository.getStudents(
                new StudentRepository.StudentsCallback() {

                    @Override
                    public void onResult(
                            List<Student> students) {

                        if (students == null) {

                            Log.e(
                                    TAG,
                                    "onResult: students list is null"
                            );

                            runOnUiThread(
                                    new Runnable() {

                                        @Override
                                        public void run() {

                                            tvStudentCount.setText(
                                                    "Total Students: 0"
                                            );

                                            Toast.makeText(
                                                    StudentListActivity.this,
                                                    "No student data was returned.",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
                            );

                            return;
                        }

                        Log.d(
                                TAG,
                                "onResult: Received "
                                        + students.size()
                                        + " students"
                        );

                        runOnUiThread(
                                new Runnable() {

                                    @Override
                                    public void run() {

                                        if (students.isEmpty()) {

                                            tvStudentCount.setText(
                                                    "Total Students: 0"
                                            );

                                            Toast.makeText(
                                                    StudentListActivity.this,
                                                    "No students found. Please register a student.",
                                                    Toast.LENGTH_LONG
                                            ).show();

                                            Log.d(
                                                    TAG,
                                                    "onResult: No students found"
                                            );

                                        } else {

                                            tvStudentCount.setText(
                                                    "Total Students: "
                                                            + students.size()
                                            );

                                            Toast.makeText(
                                                    StudentListActivity.this,
                                                    "Found "
                                                            + students.size()
                                                            + " students",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            Log.d(
                                                    TAG,
                                                    "onResult: Displaying "
                                                            + students.size()
                                                            + " students"
                                            );
                                        }

                                        adapter =
                                                new StudentAdapter(
                                                        students
                                                );

                                        adapter.setOnStudentClickListener(
                                                new StudentAdapter.OnStudentClickListener() {

                                                    @Override
                                                    public void onStudentClick(
                                                            Student student) {

                                                        Intent intent =
                                                                new Intent(
                                                                        StudentListActivity.this,
                                                                        StudentDetailsActivity.class
                                                                );

                                                        intent.putExtra(
                                                                "student",
                                                                student
                                                        );

                                                        startActivity(intent);
                                                    }
                                                }
                                        );

                                        recyclerView.setAdapter(
                                                adapter
                                        );

                                        Log.d(
                                                TAG,
                                                "onResult: Adapter set with "
                                                        + students.size()
                                                        + " items"
                                        );
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(
                            Exception exception) {

                        Log.e(
                                TAG,
                                "onError: ",
                                exception
                        );

                        runOnUiThread(
                                new Runnable() {

                                    @Override
                                    public void run() {

                                        String errorMessage =
                                                exception != null
                                                        ? exception.getMessage()
                                                        : "Unknown error";

                                        Toast.makeText(
                                                StudentListActivity.this,
                                                "Error: "
                                                        + errorMessage,
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                }
                        );
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(
            Menu menu) {

        getMenuInflater().inflate(
                R.menu.toolbar_menu,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {

        if (item.getItemId()
                == android.R.id.home) {

            finish();

            return true;
        }

        if (item.getItemId()
                == R.id.action_exit) {

            finishAffinity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}