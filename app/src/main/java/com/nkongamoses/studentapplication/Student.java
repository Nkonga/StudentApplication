package com.nkongamoses.studentapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "students")
public class Student implements Serializable {

    @PrimaryKey
    @androidx.annotation.NonNull
    private String studentId;
    private String firstName;
    private String lastName;
    private String grade;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String profileImagePath;

    public Student(String studentId,
                   String firstName,
                   String lastName,
                   String grade,
                   String gender,
                   String dateOfBirth,
                   String phone) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.profileImagePath = null;
    }

    @androidx.annotation.NonNull
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(@androidx.annotation.NonNull String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}