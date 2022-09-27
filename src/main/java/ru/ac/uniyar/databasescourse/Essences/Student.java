package ru.ac.uniyar.databasescourse.Essences;

public class Student {
    private int studentId;
    private int solutionId;
    private int reviewerId;
    private String studentName;
    private String studentSurname;
    private String hasPassed;

    public Student(
        int studentId,
        int solutionId,
        int reviewerId,
        String studentName,
        String studentSurname,
        String hasPassed
    ) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.solutionId = solutionId;
        this.reviewerId = reviewerId;
        this.hasPassed = hasPassed;
    }
}
