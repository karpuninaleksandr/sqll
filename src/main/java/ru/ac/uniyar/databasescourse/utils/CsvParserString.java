package ru.ac.uniyar.databasescourse.utils;

import ru.ac.uniyar.databasescourse.essences.Reviewer;
import ru.ac.uniyar.databasescourse.essences.Solution;
import ru.ac.uniyar.databasescourse.essences.Student;

import java.sql.Connection;
import java.sql.SQLException;

public class CsvParserString {
    public int studentId;
    public String studentName;
    public String studentSurname;
    public int solutionId;
    public int reviewerId;
    public String reviewerSurname;
    public String reviewerDepartment;
    public double score;
    public String hasPassed;

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

    public void fill(Connection connection) {
        try {
            new Student(this.studentId, this.studentName, this.studentSurname).addToTable(connection)
                    .executeQuery().close();
            new Reviewer(this.reviewerId, this.reviewerSurname, this.reviewerDepartment).addToTable(connection)
                    .executeQuery().close();
            new Solution(this.solutionId, this.studentId, this.reviewerId, this.score, this.hasPassed).addToTable(connection)
                    .executeQuery().close();
        } catch (SQLException ex) {
            System.out.printf("Statement execution error: %s\n", ex);
        }

    }
}
