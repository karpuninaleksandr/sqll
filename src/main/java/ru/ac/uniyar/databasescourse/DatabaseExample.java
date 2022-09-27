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

    public static void selectSQL(String query, String additionalQuery) {
        ResultSet rs = createQuery((additionalQuery == null) ? ("SELECT " + query + " FROM students") : ("SELECT " + query + " FROM students WHERE " + additionalQuery));
        try {
            while (rs.next()) {
                for (int i = 1; i < 8; ++i) {
                    try {
                        System.out.printf("%s ", rs.getString(i));
                    } catch (SQLException ex) {
                        break;
                    }
                }
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.out.printf("Statement execution error: %s\n", ex);
        }
    }

    public static void insertSQL(String values) {
        ResultSet rs = createQuery("INSERT INTO students (name, surname, card, answer, score, review, has_pass)\n VALUES " + values);
        try {
            while (rs.next()) System.out.printf("Statement %s\n", rs.getString(1));
        } catch (SQLException ex) {
            System.out.printf("Statement execution error: %s\n", ex);
        }
    }

    public static void createTableSQL() {
        createQuery("CREATE TABLE IF NOT EXISTS students(\n" +
                "name CHAR(127) NOT NULL,\n" +
                "surname CHAR(127) NOT NULL,\n" +
                "card INT PRIMARY KEY,\n" +
                "answer CHAR(127) NOT NULL,\n" +
                "score DOUBLE NOT NULL,\n" +
                "review CHAR(127),\n" +
                "has_pass CHAR(1)\n" +
                ")\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci;");
    }

    public static void main(String[] args) throws IOException {
        CsvDataLoader someCsvDataLoader = new CsvDataLoader();
        someCsvDataLoader.load(Path.of("data.csv"));


//        createQuery("SHOW TABLES");
//        insertSQL("('Иван', 'Петров', 762201, 'Александр Сергеевич Пушкин — солнце русской поэзии!', 3.3, 'Хорошо, но мало', 'F')");
//        insertSQL("('Дмитрий', 'Степанов', 762101, 'Фёдор Михайлович Достоевский написал много книг.', 4.5, 'Мало, но хорошо.', 'T')");
//        insertSQL("('Степан', 'Михайлов', 762202, 'Статья в Wikipedia про Валерия Брюсова', 5.0, null, 'T')");
//        insertSQL("('Михаил', 'Сергеев', 762016, 'Александр Сергеевич Грибоедов — солнце русской поэзии!', 4.0, 'Хорошо, но мало', null)");
//        insertSQL("('Сергей', 'Кириллов', 762203, 'Не умею читать', 2.2, null, 'F')");
//        insertSQL("('Кирилл', 'Иванов', 762204, 'Толстой — грязное пятно на теле русской литературы', 3.6, 'Неправда.', null)");
//        selectSQL("*", null);
//        checkHasPassIsNull();


    }

//    private static void checkHasPassIsNull() {
//        selectSQL("name, answer", "has_pass is null");
//    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, user, password);
    }
}