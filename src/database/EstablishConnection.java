package database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EstablishConnection {
    private final String dbConnectionURL = "jdbc:sqlite:src\\resource\\userDB.db";
    private final String dbUser = "";
    private final String dbPassword = "";

    private int playerPoints;
    private int playerHighscore;
    private int playerLength;
    private int playerBestLength;
    private ArrayList<String[]> highscoreTable;
    private String playerName;
    private String playerPassword;

    public final int QUERYPLAYER = 0;
    public final int QUERYPLAYERPOINTS = 1;
    public final int QUERYHIGHSCORELIST = 2;
    public final int ADDPLAYER = 3;
    public final int UPDATEPLAYERPOINTS = 4;

    public EstablishConnection() {
    }

    public EstablishConnection(String name, String password) {
        this.playerName = name;
        this.playerPassword = password;
    }

    private String checkUserLogin() {
        return "select * from player where name = '" + this.playerName + "' and password = '" + this.playerPassword
                + "'";
    }

    private String queryPoints() {
        return "select points, length from player where name = '" + this.playerName + "' and password = '"
                + this.playerPassword + "'";
    }

    private String addUser() {
        return "insert into player (name, points, password) values ('" + this.playerName + "', 0, '"
                + this.playerPassword + "')";
    }

    private String updatePoints(int points, int length) {
        return "update player set points = '" + points + "', length = '" + length + "' where name = '" + this.playerName
                + "' and password = '" + this.playerPassword + "'";
    }

    private void queryHighscoreTable(Statement statement) {
        int columnCount = 0;
        this.highscoreTable = new ArrayList<String[]>();
        try {
            ResultSet result = statement.executeQuery("select name, points from player");
            columnCount = result.getMetaData().getColumnCount();
            while (result.next()) {
                String[] temp = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    temp[j] = result.getString(j + 1);
                }
                highscoreTable.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sqlRequest(int operation) {
        boolean status = false;
        ResultSet result = null;
        try {
            DriverManager.setLogWriter(new PrintWriter(System.out));
            Class.forName("org.sqlite.JDBC");
            // System.out.println("Treiber geladen");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Treiber konnten nicht geladen werden");
        }
        try (Connection connection = DriverManager.getConnection(dbConnectionURL, dbUser, dbPassword)) {
            try (Statement statement = connection.createStatement()) {
                switch (operation) {
                case 0:
                    result = statement.executeQuery(this.checkUserLogin());
                    status = result.next();
                    break;
                case 1:
                    result = statement.executeQuery(this.queryPoints());
                    this.playerHighscore = result.getInt(1);
                    this.playerBestLength = result.getInt(2);
                    break;
                case 2:
                    queryHighscoreTable(statement);
                    break;
                case 3:
                    statement.executeUpdate(this.addUser());
                    break;
                case 4:
                    statement.executeUpdate(updatePoints(this.playerPoints, this.playerLength));
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
        return status;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerPassword() {
        return playerPassword;
    }

    public void setPlayerPassword(String playerPassword) {
        this.playerPassword = playerPassword;
    }

    public int getPlayerHighscore() {
        sqlRequest(this.QUERYPLAYERPOINTS);
        return this.playerHighscore;
    }

    public void setPlayerHighscore(int playerHighscore) {
        this.playerHighscore = playerHighscore;
    }

    public ArrayList<String[]> getHighscoreTable() {
        sqlRequest(this.QUERYHIGHSCORELIST);
        return highscoreTable;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public int getPlayerLength() {
        return playerLength;
    }

    public void setPlayerLength(int playerLength) {
        this.playerLength = playerLength;
    }

    public int getPlayerBestLength() {
        sqlRequest(this.QUERYPLAYERPOINTS);
        return this.playerBestLength;
    }

    public void setPlayerBestLength(int playerBestLength) {
        this.playerBestLength = playerBestLength;
    }

}
