package ru.ac.uniyar.databasescourse.Essences;

public class Teacher {
    private int reviewerId;
    private String reviewerSurname;
    private String reviewerDepartment;

    public Teacher(
        int reviewerId,
        String reviewerSurname,
        String reviewerDepartment
    ) {
        this.reviewerId = reviewerId;
        this.reviewerSurname = reviewerSurname;
        this.reviewerDepartment = reviewerDepartment;
    }
}
