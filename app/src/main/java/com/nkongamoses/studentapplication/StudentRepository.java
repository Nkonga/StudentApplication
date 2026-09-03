package com.nkongamoses.studentapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {

    private static final String TAG = "StudentRepository";
    private static StudentRepository instance;

    private final StudentDao studentDao;
    private final ExecutorService databaseExecutor;
    private final Handler mainHandler;

    private StudentRepository(Context context) {
        Log.d(TAG, "StudentRepository: Initializing");
        AppDatabase database = AppDatabase.getInstance(context);
        studentDao = database.studentDao();
        databaseExecutor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized StudentRepository getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "getInstance: Creating new instance");
            instance = new StudentRepository(context.getApplicationContext());
        } else {
            Log.d(TAG, "getInstance: Returning existing instance");
        }
        return instance;
    }

    public interface SaveCallback {
        void onSuccess();
        void onError(Exception exception);
    }

    public interface StudentsCallback {
        void onResult(List<Student> students);
        void onError(Exception exception);
    }

    public void addStudent(Student student, SaveCallback callback) {
        Log.d(TAG, "addStudent: Adding student: " + student.getStudentId());
        databaseExecutor.execute(() -> {
            try {
                studentDao.insert(student);
                Log.d(TAG, "addStudent: Student inserted successfully");
                mainHandler.post(callback::onSuccess);
            } catch (Exception exception) {
                Log.e(TAG, "addStudent: Error inserting student", exception);
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void updateStudent(Student student, SaveCallback callback) {
        databaseExecutor.execute(() -> {
            try {
                studentDao.update(student);
                mainHandler.post(callback::onSuccess);
            } catch (Exception exception) {
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void deleteStudent(Student student, SaveCallback callback) {
        databaseExecutor.execute(() -> {
            try {
                studentDao.delete(student);
                mainHandler.post(callback::onSuccess);
            } catch (Exception exception) {
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void getStudents(StudentsCallback callback) {
        Log.d(TAG, "getStudents: Fetching all students");
        databaseExecutor.execute(() -> {
            try {
                List<Student> students = studentDao.getAllStudents();
                Log.d(TAG, "getStudents: Found " + students.size() + " students in database");
                
                // Print each student for debugging
                for (Student s : students) {
                    Log.d(TAG, "getStudents: Student: " + s.getFullName() + " (ID: " + s.getStudentId() + ")");
                }
                
                mainHandler.post(() -> {
                    Log.d(TAG, "getStudents: Posting result to main thread with " + students.size() + " students");
                    callback.onResult(students);
                });
            } catch (Exception exception) {
                Log.e(TAG, "getStudents: Error fetching students", exception);
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void getStudentById(String studentId, StudentsCallback callback) {
        databaseExecutor.execute(() -> {
            try {
                Student student = studentDao.getStudentById(studentId);
                List<Student> students = new java.util.ArrayList<>();
                if (student != null) {
                    students.add(student);
                }
                mainHandler.post(() -> callback.onResult(students));
            } catch (Exception exception) {
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void searchStudents(String searchQuery, StudentsCallback callback) {
        databaseExecutor.execute(() -> {
            try {
                List<Student> students = studentDao.searchStudents(searchQuery);
                mainHandler.post(() -> callback.onResult(students));
            } catch (Exception exception) {
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void deleteAllStudents(SaveCallback callback) {
        databaseExecutor.execute(() -> {
            try {
                studentDao.deleteAll();
                mainHandler.post(callback::onSuccess);
            } catch (Exception exception) {
                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void shutdown() {
        databaseExecutor.shutdown();
    }
}