package ru.ac.uniyar.databasescourse;

import ru.ac.uniyar.databasescourse.essences.Reviewer;
import ru.ac.uniyar.databasescourse.essences.Solution;
import ru.ac.uniyar.databasescourse.essences.Student;
import ru.ac.uniyar.databasescourse.utils.CsvDataLoader;

import java.nio.file.Path;
import java.sql.*;

public class DatabaseExample {
    private static final String URL = String.format("jdbc:mariadb://%s", System.getenv("MARIADB_HOST"));
    private static final String user = System.getenv("MARIADB_USER");
    private static final String password = System.getenv("MARIADB_PASSWORD");
    private static Connection connection;

    public static ResultSet createQuery(String query) {
            try (Statement smt = connection.createStatement()) {
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
        return null;
    }

    public static void selectSQL(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
//        PreparedStatement preparedStatement =  connection.prepareStatement(query);
//        preparedStatement.setString(1, tableName);
//        ResultSet rs = preparedStatement.executeQuery();
//        preparedStatement.close();
        ResultSet rs = createQuery(query);
        int amountOfColumns = (tableName.equals("solutions")) ? 5 : 3;
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

    public static void addStudent(Student student) throws SQLException {
        String query = "INSERT IGNORE INTO students (studentID, studentName, studentSurname) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, student.studentID);
        preparedStatement.setString(2, student.studentName);
        preparedStatement.setString(3, student.studentSurname);
        preparedStatement.executeQuery();
        preparedStatement.close();
    }

    public static void addReviewer(Reviewer reviewer) throws SQLException {
        String query = "INSERT IGNORE INTO reviewers (reviewerID, reviewerSurname, reviewerDepartment) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, reviewer.reviewerID);
        preparedStatement.setString(2, reviewer.reviewerSurname);
        preparedStatement.setString(3, reviewer.reviewerDepartment);
        preparedStatement.executeQuery();
        preparedStatement.close();
    }

    public static void addSolution(Solution solution) throws SQLException {
        String query = "INSERT IGNORE INTO solutions (solutionID, studentID, reviewerID, score, hasPassed) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, solution.solutionID);
        preparedStatement.setInt(2, solution.studentID);
        preparedStatement.setInt(3, solution.reviewerID);
        preparedStatement.setDouble(4, solution.score);
        preparedStatement.setString(5, solution.hasPassed);
        preparedStatement.executeQuery();
        preparedStatement.close();
    }

    public static void createTablesSQL() {
        System.out.println("Creating new tables...");
        createQuery("CREATE TABLE IF NOT EXISTS students(\n" +
                "studentID INT NOT NULL PRIMARY KEY,\n" +
                "studentName CHAR(127) NOT NULL,\n" +
                "studentSurname CHAR(127) NOT NULL\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
        createQuery("CREATE TABLE IF NOT EXISTS reviewers(\n" +
                "reviewerID INT NOT NULL PRIMARY KEY,\n" +
                "reviewerSurname CHAR(127) NOT NULL,\n" +
                "reviewerDepartment CHAR(127) NOT NULL\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
        createQuery("CREATE TABLE IF NOT EXISTS solutions(\n" +
                "solutionID INT NOT NULL PRIMARY KEY,\n" +
                "studentID INT NOT NULL,\n" +
                "reviewerID INT NOT NULL,\n" +
                "score DOUBLE NOT NULL,\n" +
                "hasPassed CHAR(3),\n" +
                "FOREIGN KEY (studentID) REFERENCES students(studentID) ON DELETE CASCADE\n," +
                "FOREIGN KEY (reviewerID) REFERENCES reviewers(reviewerID) ON DELETE CASCADE\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
        System.out.println("Created new tables");
    }

    public static void dropTablesSQL() {
        System.out.println("Dropping all tables...");
        createQuery("DROP TABLE IF EXISTS solutions, students, reviewers");
        System.out.println("Dropped all tables");
    }

    public static void fillTablesSQL() {
        System.out.println("Filling tables from csv file...");
        CsvDataLoader.parseCsvFile(Path.of("data.csv")).forEach(csvParserString -> {
            try {
                addStudent(new Student(
                        csvParserString.getStudentId(),
                        csvParserString.getStudentName(),
                        csvParserString.getStudentSurname())
                );
                addReviewer(new Reviewer(
                        csvParserString.getReviewerId(),
                        csvParserString.getReviewerSurname(),
                        csvParserString.getReviewerDepartment())
                );
                addSolution(new Solution(
                        csvParserString.getSolutionId(),
                        csvParserString.getStudentId(),
                        csvParserString.getReviewerId(),
                        csvParserString.getScore(),
                        csvParserString.getHasPassed())
                );
            } catch (SQLException ex) {
                System.out.printf("Statement execution error: %s\n", ex);
            }
        });
        System.out.println("Filled all tables");
    }

    public static void main(String[] args) throws SQLException {
        connection = createConnection();
//        dropTablesSQL();
//        createTablesSQL();
//        fillTablesSQL();

//        selectSQL("students");
//        selectSQL("solutions");
//        selectSQL("reviewers");

        getMaxMinScores();
        getMinMaxScoresFromReviewers();
        getScoreStatistics();
        getCheckStatistics();
    }

    private static void getMaxMinScores() {
        System.out.println("\n=== All students with either max or min score ===");
        String query =  "WITH minMax AS (SELECT max(solutions.score) AS max, min(solutions.score) AS min FROM solutions) SELECT solutions.score, " +
                "students.studentName, students.studentSurname FROM solutions " +
                "INNER JOIN students ON (solutions.studentID = students.studentID)" +
                "INNER JOIN minMax ON (max = solutions.score OR min = solutions.score);";
        ResultSet rs = createQuery(query);
        try {
            while (rs.next()) {
                for (int i = 1; i <= 3; ++i) {
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
        System.out.println("===============");
    }

    private static void getMinMaxScoresFromReviewers() {
        System.out.println("\n=== All reviewers with either max or min given score ===");
        String query =  "WITH minMax AS (SELECT max(solutions.score) AS max, min(solutions.score) AS min FROM solutions) " +
                "SELECT DISTINCT solutions.score, reviewers.reviewerSurname FROM solutions " +
                "INNER JOIN reviewers ON (solutions.reviewerID = reviewers.reviewerID)" +
                "INNER JOIN minMax ON (max = solutions.score OR min = solutions.score);";
        ResultSet rs = createQuery(query);
        try {
            while (rs.next()) {
                for (int i = 1; i <= 2; ++i) {
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
        System.out.println("===============");
    }

    private static void getScoreStatistics() {
        System.out.println("=== Score statistics ===");
        String query = "(SELECT reviewers.reviewerSurname, students.studentName, students.studentSurname, AVG(solutions.score) " +
            "FROM solutions INNER JOIN students ON (students.studentID = solutions.studentID) " +
            "INNER JOIN reviewers ON (reviewers.reviewerID = solutions.reviewerID)" +
            "GROUP BY students.studentName, students.studentSurname, reviewers.reviewerSurname)" +
            "ORDER BY reviewers.reviewerSurname";
        ResultSet rs = createQuery(query);
        try {
            while (rs.next()) {
                for (int i = 1; i <= 4; ++i) {
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
        System.out.println("===============");
    }

    private static void getCheckStatistics() {
        System.out.println("=== Checking statistics ===");
        String query = "(SELECT reviewers.reviewerSurname, students.studentName, students.studentSurname, COUNT(solutions.score) " +
                "FROM solutions INNER JOIN students ON (students.studentID = solutions.studentID) " +
                "INNER JOIN reviewers ON (reviewers.reviewerID = solutions.reviewerID)" +
                "GROUP BY students.studentName, students.studentSurname, reviewers.reviewerSurname)" +
                "ORDER BY reviewers.reviewerSurname";
        ResultSet rs = createQuery(query);
        try {
            while (rs.next()) {
                for (int i = 1; i <= 4; ++i) {
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
        System.out.println("===============");
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, user, password);
    }
}