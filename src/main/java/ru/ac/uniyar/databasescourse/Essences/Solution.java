package ru.ac.uniyar.databasescourse.Essences;

public class Solution {
    private int solutionId;
    private int studentId;
    private int reviewerId;
    private int score;

    public Solution(int solutionId, int studentId, int reviewerId, int score) {
        this.solutionId = solutionId;
        this.studentId = studentId;
        this.reviewerId = reviewerId;
        this.score = score;
    }
}
