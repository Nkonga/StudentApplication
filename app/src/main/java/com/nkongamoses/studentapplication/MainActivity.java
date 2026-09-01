package com.nkongamoses.studentapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnRegisterStudent;
    private Button btnViewStudents;
    private Button btnAbout;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect Home screen buttons
        btnRegisterStudent = findViewById(R.id.btnRegisterStudent);
        btnViewStudents = findViewById(R.id.btnViewStudents);
        btnAbout = findViewById(R.id.btnAbout);
        btnExit = findViewById(R.id.btnExit);

        // Open Student Registration
        btnRegisterStudent.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    StudentRegistrationActivity.class
            );
            startActivity(intent);
        });

        // Open Student List
        btnViewStudents.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    StudentListActivity.class
            );
            startActivity(intent);
        });

        // About
        btnAbout.setOnClickListener(v -> showAboutDialog());

        // Exit application
        btnExit.setOnClickListener(v -> showExitConfirmation());
    }

    private void showAboutDialog() {

        new AlertDialog.Builder(this)
                .setTitle("About Student Application")
                .setMessage(
                        "Student Application\n\n" +
                                "A simple Android student management " +
                                "application developed using Java and Android Studio."
                )
                .setPositiveButton("OK", null)
                .show();
    }

    private void showExitConfirmation() {

        new AlertDialog.Builder(this)
                .setTitle("Close Application")
                .setMessage("Are you sure you want to close the application?")
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("EXIT", (dialog, which) -> {
                    finishAffinity();
                })
                .show();
    }
}