package ru.ac.uniyar.databasescourse.essences;

public class Student {
    public int studentID;
    public String studentName;
    public String studentSurname;

    public Student(int studentID, String studentName, String studentSurname) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
    }
}
