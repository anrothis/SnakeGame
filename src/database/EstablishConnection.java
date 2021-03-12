package database;

import org.sqlite.JDBC;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EstablishConnection {
    private final String dbConnectionURL = "jdbc:sqlite::resource:userDB.db";
    private final String dbUser = "";
    private final String dbPassword = "";
    private ResultSet result;

    private static final int QUERY = 0;
    private static final int ADD = 1;
    private static final int UPDATE = 2;

    public boolean checkUserLogin(String name, String password) {
        sqlRequest(QUERY, "select " + name + "," + password + " from player");

        System.out.println(result);

        return true;
    }

    public void sqlRequest(int operation, String sql) {

        try {
            DriverManager.setLogWriter(new PrintWriter(System.out));
            Class.forName("org.sqlite.JDBC");
            System.out.println("Treiber geladen");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Treiber konnten nicht geladen werden");
        }
        try (Connection connection = DriverManager.getConnection(dbConnectionURL, dbUser, dbPassword)) {
            try (Statement statement = connection.createStatement()) {
                switch (operation) {
                case 0:
                    result = statement.executeQuery(sql);
                    break;
                case 1:
                    statement.executeUpdate(sql);
                    break;
                case 2:
                    statement.executeUpdate(sql);
                    break;
                default:
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Zugriff auf die Datenbank");
        }
    }
}
