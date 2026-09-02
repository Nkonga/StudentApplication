package com.nkongamoses.studentapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students ORDER BY firstName ASC, lastName ASC")
    List<Student> getAllStudents();

    @Query("SELECT * FROM students WHERE studentId = :studentId LIMIT 1")
    Student getStudentById(String studentId);

    @Query("SELECT * FROM students WHERE firstName LIKE '%' || :searchQuery || '%' " +
            "OR lastName LIKE '%' || :searchQuery || '%' " +
            "OR studentId LIKE '%' || :searchQuery || '%' " +
            "ORDER BY firstName ASC")
    List<Student> searchStudents(String searchQuery);

    @Query("DELETE FROM students")
    void deleteAll();
}