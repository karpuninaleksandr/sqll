package ru.ac.uniyar.databasescourse.utils;

public class CsvParserString {
    private int studentId;
    private String studentName;
    private String studentSurname;
    private int solutionId;
    private int reviewerId;
    private String reviewerSurname;
    private String reviewerDepartment;
    private double score;
    private String hasPassed;

    public CsvParserString(
        int studentId,
        String studentName,
        String studentSurname,
        int solutionId,
        int reviewerId,
        String reviewerSurname,
        String reviewerDepartment,
        double score,
        String hasPassed
    ) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.solutionId = solutionId;
        this.reviewerId = reviewerId;
        this.reviewerSurname = reviewerSurname;
        this.reviewerDepartment = reviewerDepartment;
        this.score = score;
        this.hasPassed = hasPassed;
    }

    public int getStudentId() {
        return this.studentId;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public String getStudentSurname() {
        return this.studentSurname;
    }
    public int getSolutionId() {
        return this.solutionId;
    }
    public int getReviewerId() {
        return this.reviewerId;
    }
    public String getReviewerSurname() {
        return this.reviewerSurname;
    }
    public String getReviewerDepartment() {
        return this.reviewerDepartment;
    }
    public double getScore() {
        return this.score;
    }
    public String getHasPassed() {
        return this.hasPassed;
    }


}
