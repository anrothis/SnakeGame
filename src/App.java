import database.EstablishConnection;

public class App {
    public static void main(String[] args) throws Exception {

        final EstablishConnection eC = new EstablishConnection();
        new LoginScreen(eC);

        // new GameScreen();

        // testDB();

    }

    public void testDB() {
        EstablishConnection con = new EstablishConnection("tom", "password");

        System.out.println("----Checking Tom");
        System.out.println("Spieler Tom vorhanden: " + con.sqlRequest(con.QUERYPLAYER));

        System.out.println("-----Checking Peter");
        con.setPlayerName("peter");
        con.setPlayerPassword("test");
        System.out.println("Spieler Peter vorhanden: " + con.sqlRequest(con.QUERYPLAYER));

        if (!con.sqlRequest(con.QUERYPLAYER)) {
            System.out.println("###### Add Peter");
            con.sqlRequest(con.ADDPLAYER);
            System.out.println("Perter added");
        }

        System.out.println("###### Get Points Peter");
        System.out.println(con.getPlayerHighscore());
        con.setPlayerPoints(15);

        System.out.println("###### Update Points Peter");
        con.sqlRequest(con.UPDATEPLAYERPOINTS);
        System.out.println(con.getPlayerHighscore());

        System.out.println("###### Get Score List");
        for (String string[] : con.getHighscoreTable()) {
            System.out.println(string[0] + ": " + string[1]);
        }
    }
}
