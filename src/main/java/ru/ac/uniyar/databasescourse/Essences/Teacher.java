package ru.ac.uniyar.databasescourse.Essences;

public class Teacher {
    private int reviewerId;
    private int studentId;
    private int solutionId;
    private String reviewerSurname;
    private String reviewerDepartment;

    public Teacher(
        int reviewerId,
        int studentId,
        int solutionId,
        String reviewerSurname,
        String reviewerDepartment
    ) {
        this.reviewerId = reviewerId;
        this.studentId = studentId;
        this.solutionId = solutionId;
        this.reviewerSurname = reviewerSurname;
        this.reviewerDepartment = reviewerDepartment;
    }
}
