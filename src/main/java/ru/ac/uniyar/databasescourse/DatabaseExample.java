package ru.ac.uniyar.databasescourse;

import ru.ac.uniyar.databasescourse.utils.CsvDataLoader;

import java.nio.file.Path;
import java.sql.*;

public class DatabaseExample {
    private static final String URL = String.format("jdbc:mariadb://%s", System.getenv("MARIADB_HOST"));
    private static final String user = System.getenv("MARIADB_USER");
    private static final String password = System.getenv("MARIADB_PASSWORD");
    private static Connection connection;

    private static ResultSet createQuery(String query) {
            try (Statement smt = connection.createStatement()) {
                try {
                    smt.executeQuery("USE AleksandrKarpuninDB");
                    return smt.executeQuery(query);
                } catch (SQLException ex) {
                    System.out.printf("Statement execution error: %s\n", ex);
                }
            } catch (SQLException ex) {
                System.out.printf("Can't create statement: %s\n", ex);
            }
        return null;
    }

    private static void selectSQL(String tableName) {
        String query = "SELECT * FROM " + tableName;
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

    private static void createTablesSQL() {
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

    private static void dropTablesSQL() {
        System.out.println("Dropping all tables...");
        createQuery("DROP TABLE IF EXISTS solutions, students, reviewers");
        System.out.println("Dropped all tables");
    }

    private static void fillTablesSQL() {
        System.out.println("Filling tables from csv file...");
        CsvDataLoader.parseCsvFile(Path.of("data.csv")).forEach(csvParserString-> csvParserString.fill(connection));
        System.out.println("Filled all tables");
    }

    public static void main(String[] args) throws SQLException {
        connection = createConnection();

        prepareToWork();

        selectAllInfoFromAllTables();

        aggregatingTask();
    }

    private static void prepareToWork() {
        dropTablesSQL();
        createTablesSQL();
        fillTablesSQL();
    }

    private static void selectAllInfoFromAllTables() throws SQLException {
        System.out.println("=== Info from students table ===");
        selectSQL("students");
        System.out.println("===============");
        System.out.println("=== Info from solutions table ===");
        selectSQL("solutions");
        System.out.println("===============");
        System.out.println("=== Info from reviewers table ===");
        selectSQL("reviewers");
        System.out.println("===============");
    }

    private static void aggregatingTask() {
        getMaxMinScores();
        getMinMaxScoresFromReviewers();
        getScoreStatistics();
        getCheckStatistics();
    }

    private static void getMaxMinScores() {
        System.out.println("\n=== All students with either max or min score ===");
        String query =  "WITH minMax AS " +
                "   (SELECT max(solutions.score) AS max, min(solutions.score) AS min FROM solutions) " +
                "SELECT solutions.score, " +
                "students.studentName, students.studentSurname FROM solutions " +
                "JOIN students ON (solutions.studentID = students.studentID)" +
                "JOIN minMax ON (max = solutions.score OR min = solutions.score);";
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
            "GROUP BY students.studentID, reviewers.reviewerID)" +
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
        String query = "(SELECT reviewers.reviewerSurname, students.studentName, students.studentSurname, COUNT(solutions.score) AS countScore " +
                "FROM solutions INNER JOIN students ON (students.studentID = solutions.studentID) " +
                "INNER JOIN reviewers ON (reviewers.reviewerID = solutions.reviewerID)" +
                "GROUP BY students.studentID, reviewers.reviewerID)" +
                "ORDER BY countScore DESC ";
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