package ru.ac.uniyar.databasescourse.essences;

public class Solution {
    public int solutionID;
    public int studentID;
    public int reviewerID;
    public double score;
    public String hasPassed;

    public Solution(int solutionID, int studentID, int reviewerID, double score, String hasPassed) {
        this.solutionID = solutionID;
        this.studentID = studentID;
        this.reviewerID = reviewerID;
        this.score = score;
        this.hasPassed = hasPassed;
    }
}
