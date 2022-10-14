package ru.ac.uniyar.databasescourse.essences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public PreparedStatement addToTable(Connection connection) throws SQLException {
        String query = "INSERT IGNORE INTO solutions (solutionID, studentID, reviewerID, score, hasPassed) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, this.solutionID);
        preparedStatement.setInt(2, this.studentID);
        preparedStatement.setInt(3, this.reviewerID);
        preparedStatement.setDouble(4, this.score);
        preparedStatement.setString(5, this.hasPassed);
        return preparedStatement;
    }
}
