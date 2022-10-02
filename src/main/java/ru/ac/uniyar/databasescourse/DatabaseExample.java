package ru.ac.uniyar.databasescourse;

import ru.ac.uniyar.databasescourse.utils.CsvDataLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;

public class DatabaseExample {
    private static final String URL = String.format("jdbc:mariadb://%s", System.getenv("MARIADB_HOST"));
    private static final String user = System.getenv("MARIADB_USER");
    private static final String password = System.getenv("MARIADB_PASSWORD");

    public static ResultSet createQuery(String query) {
        try (Connection conn = createConnection()) {
            try (Statement smt = conn.createStatement()) {
                try {
                    smt.executeQuery("USE AleksandrKarpuninDB");
                    return smt.executeQuery(query);
                }
                catch (SQLException ex) {
                    System.out.printf("Statement execution error: %s\n", ex);
                }
            }
            catch (SQLException ex) {
                System.out.printf("Can't create statement: %s\n", ex);
            }
        }
        catch (SQLException ex) {
            System.out.printf("Can't create connection: %s\n", ex);
        }
        return null;
    }

    public static void selectSQL(String name, String query, String additionalQuery) {
        ResultSet rs = createQuery((additionalQuery == null) ? ("SELECT " + query + " FROM " + name)
                : ("SELECT " + query + " FROM " + name + " WHERE " + additionalQuery));
        int amountOfColumns = (name.equals("solutions")) ? 4 : 3;
        try {
            while (rs.next()) {
                for (int i = 1; i <= amountOfColumns; ++i) {
                    try {
                        System.out.printf("%s ", rs.getString(i));
                    } catch (SQLException ex) {
                        break;
                    }
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            System.out.printf("Statement execution error: %s\n", ex);
        }
    }

    public static void insertSQL(String name, String columns, String values) {
        ResultSet rs = createQuery("INSERT IGNORE INTO " + name + " (" + columns +
                ")\n VALUES (" + values + ")");
        try {
            while (rs.next()) System.out.printf("Statement %s\n", rs.getString(1));
        } catch (SQLException ex) {
            System.out.printf("Statement execution error: %s\n", ex);
        }
    }

    public static void createTablesSQL() {
        System.out.println("Creating new tables...");
        createQuery("CREATE TABLE IF NOT EXISTS students(\n" +
                "studentID INT PRIMARY KEY,\n" +
                "studentName CHAR(127) NOT NULL,\n" +
                "studentSurname CHAR(127) NOT NULL\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
        createQuery("CREATE TABLE IF NOT EXISTS solutions(\n" +
                "solutionID INT PRIMARY KEY,\n" +
                "studentID INT NOT NULL,\n" +
                "reviewerID INT NOT NULL,\n" +
                "score DOUBLE NOT NULL,\n" +
                "hasPassed CHAR(3)\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
        createQuery("CREATE TABLE IF NOT EXISTS reviewers(\n" +
                "reviewerID INT PRIMARY KEY,\n" +
                "reviewerSurname CHAR(127) NOT NULL,\n" +
                "reviewerDepartment CHAR(127) NOT NULL\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
        System.out.println("Created new tables");
    }

    public static void dropTablesSQL() {
        System.out.println("Dropping all tables...");
        createQuery("DROP TABLE students");
        createQuery("DROP TABLE solutions");
        createQuery("DROP TABLE reviewers");
        System.out.println("Dropped all tables");
    }

    public static void fillTablesSQL() {
        System.out.println("Filling tables from csv file...");
        CsvDataLoader.parseCsvFile(Path.of("data.csv")).forEach(csvParserString -> {
            insertSQL("students", "studentId, studentName, studentSurname",
                    csvParserString.getStudentId() + ", \"" + csvParserString.getStudentName() + "\", \""
                            + csvParserString.getStudentSurname() + "\"");
            insertSQL("solutions", "solutionID, studentId, reviewerID, score, hasPassed",
                    csvParserString.getSolutionId() + ", " + csvParserString.getStudentId() + ", "
                            + csvParserString.getReviewerId() + ", " + csvParserString.getScore() + ", \""
                            + csvParserString.getHasPassed() + "\"");
            insertSQL("reviewers", "reviewerID, reviewerSurname, reviewerDepartment",
                    csvParserString.getReviewerId() + ", \"" + csvParserString.getReviewerSurname() + "\", \""
                            + csvParserString.getReviewerDepartment() + "\"");
        });
        System.out.println("Filled all tables");
    }

    public static void main(String[] args) throws IOException {
        dropTablesSQL();
        createTablesSQL();
        fillTablesSQL();
        selectSQL("students", "*", null);
        selectSQL("solutions", "*", null);
        selectSQL("reviewers", "*", null);
//        ResultSet rs = createQuery("SHOW TABlES");
//        try {
//            while (rs.next()) System.out.printf("Statement %s\n", rs.getString(1));
//        } catch (SQLException ex) {
//            System.out.printf("Statement execution error: %s\n", ex);
//        }
    }

    private static void checkHasPassIsNull() {
        //selectSQL("name, answer", "has_pass is null");
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, user, password);
    }
}