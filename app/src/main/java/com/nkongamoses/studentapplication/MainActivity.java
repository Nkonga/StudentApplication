package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnRegisterStudent;
    private Button btnViewStudents;
    private Button btnDashboard;

    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent the application from drawing under system bars
        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        // Status bar
        getWindow().setStatusBarColor(Color.BLACK);

        setContentView(R.layout.activity_main);

        // =========================
        // ROOT LAYOUT
        // =========================

        androidx.appcompat.widget.Toolbar toolbar =
                findViewById(R.id.toolbar);

        android.view.View mainRoot =
                findViewById(R.id.mainRoot);

        // Move the ENTIRE application below the status bar
        ViewCompat.setOnApplyWindowInsetsListener(
                mainRoot,
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

        // =========================
        // TOOLBAR
        // =========================

        if (toolbar != null) {

            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {

                getSupportActionBar()
                        .setDisplayShowTitleEnabled(false);
            }
        }

        // =========================
        // BUTTONS
        // =========================

        btnRegisterStudent =
                findViewById(R.id.btnRegisterStudent);

        btnViewStudents =
                findViewById(R.id.btnViewStudents);

        btnDashboard =
                findViewById(R.id.btnDashboard);

        // Register Student
        btnRegisterStudent.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    StudentRegistrationActivity.class
            );

            startActivity(intent);
        });

        // View Students
        btnViewStudents.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    StudentListActivity.class
            );

            startActivity(intent);
        });

        // Dashboard
        btnDashboard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    DashboardActivity.class
            );

            startActivity(intent);
        });
    }

    // =========================
    // HOME MENU
    // =========================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.home_menu,
                menu
        );

        return true;
    }

    // =========================
    // MENU ACTIONS
    // =========================

    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {

            Toast.makeText(
                    this,
                    "Settings selected",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        if (itemId == R.id.action_help) {

            Toast.makeText(
                    this,
                    "Help selected",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        if (itemId == R.id.action_about) {

            Toast.makeText(
                    this,
                    "Student Application",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        if (itemId == R.id.action_exit) {

            finishAffinity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // =========================
    // BACK BUTTON
    // =========================

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 >
                System.currentTimeMillis()) {

            finishAffinity();

        } else {

            Toast.makeText(
                    this,
                    "Press back again to exit",
                    Toast.LENGTH_SHORT
            ).show();

            backPressedTime =
                    System.currentTimeMillis();
        }
    }
}