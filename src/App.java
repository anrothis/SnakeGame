import database.EstablishConnection;

public class App {
    public static void main(String[] args) throws Exception {
        // new GameScreen();
        EstablishConnection con = new EstablishConnection();
        con.checkUserLogin("tom", "password");
    }
}
