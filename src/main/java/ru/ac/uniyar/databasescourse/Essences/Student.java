package ru.ac.uniyar.databasescourse.Essences;

public class Student {
    private int studentId;
    private String studentName;
    private String studentSurname;
    private String hasPassed;

    public Student(
        int studentId,
        String studentName,
        String studentSurname,
        String hasPassed
    ) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.hasPassed = hasPassed;
    }
}
