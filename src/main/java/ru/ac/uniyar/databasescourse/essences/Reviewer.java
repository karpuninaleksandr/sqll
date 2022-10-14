package ru.ac.uniyar.databasescourse.essences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Reviewer {
    public int reviewerID;
    public String reviewerSurname;
    public  String reviewerDepartment;

    public Reviewer(int reviewerID, String reviewerSurname, String reviewerDepartment) {
        this.reviewerID = reviewerID;
        this.reviewerSurname = reviewerSurname;
        this.reviewerDepartment = reviewerDepartment;
    }

    public PreparedStatement addToTable(Connection connection) throws SQLException {
        String query = "INSERT IGNORE INTO reviewers (reviewerID, reviewerSurname, reviewerDepartment) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, this.reviewerID);
        preparedStatement.setString(2, this.reviewerSurname);
        preparedStatement.setString(3, this.reviewerDepartment);
        return preparedStatement;
    }
}
