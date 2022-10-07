package ru.ac.uniyar.databasescourse.essences;

public class Reviewer {
    public int reviewerID;
    public String reviewerSurname;
    public  String reviewerDepartment;

    public Reviewer(int reviewerID, String reviewerSurname, String reviewerDepartment) {
        this.reviewerID = reviewerID;
        this.reviewerSurname = reviewerSurname;
        this.reviewerDepartment = reviewerDepartment;
    }
}
