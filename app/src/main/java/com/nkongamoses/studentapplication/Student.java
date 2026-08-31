package com.nkongamoses.studentapplication;

public class Student {

    private String studentId;
    private String firstName;
    private String lastName;
    private String grade;
    private String gender;
    private String dateOfBirth;
    private String phone;

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
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGrade() {
        return grade;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}