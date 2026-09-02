package com.nkongamoses.studentapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {

    private static StudentRepository instance;

    private final StudentDao studentDao;
    private final ExecutorService databaseExecutor;
    private final Handler mainHandler;

    private StudentRepository(Context context) {

        AppDatabase database = AppDatabase.getInstance(context);

        studentDao = database.studentDao();

        databaseExecutor = Executors.newSingleThreadExecutor();

        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized StudentRepository getInstance(Context context) {

        if (instance == null) {
            instance = new StudentRepository(context.getApplicationContext());
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

        databaseExecutor.execute(() -> {

            try {

                studentDao.insert(student);

                mainHandler.post(callback::onSuccess);

            } catch (Exception exception) {

                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void getStudents(StudentsCallback callback) {

        databaseExecutor.execute(() -> {

            try {

                List<Student> students = studentDao.getAllStudents();

                mainHandler.post(() -> callback.onResult(students));

            } catch (Exception exception) {

                mainHandler.post(() -> callback.onError(exception));
            }
        });
    }

    public void getStudentById(
            String studentId,
            StudentsCallback callback) {

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

    public void deleteStudent(
            Student student,
            SaveCallback callback) {

        databaseExecutor.execute(() -> {

            try {

                studentDao.delete(student);

                mainHandler.post(callback::onSuccess);

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