package ru.ac.uniyar.databasescourse.essences;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student {
    public int studentID;
    public String studentName;
    public String studentSurname;

    public Student(int studentID, String studentName, String studentSurname) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
    }

    public PreparedStatement addToTable(Connection connection) throws SQLException {
        String query = "INSERT IGNORE INTO students (studentID, studentName, studentSurname) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, this.studentID);
        preparedStatement.setString(2, this.studentName);
        preparedStatement.setString(3, this.studentSurname);
        return preparedStatement;
    }
}
