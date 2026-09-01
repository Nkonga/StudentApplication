package com.nkongamoses.studentapplication;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private static StudentRepository instance;

    private final List<Student> students;

    private StudentRepository() {
        students = new ArrayList<>();
    }

    public static synchronized StudentRepository getInstance() {

        if (instance == null) {
            instance = new StudentRepository();
        }

        return instance;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public int getStudentCount() {
        return students.size();
    }
}