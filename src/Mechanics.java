import java.util.ArrayList;
import javax.swing.*;

import database.EstablishConnection;

import java.awt.*;

public class Mechanics implements Runnable {

    private int takt;
    private SnakeObject snake;
    private ArrayList<SnakeObject> body;
    private int steps, screenSize;
    private boolean eaten, gameStarted;
    private Point snakeDirection;
    protected GameScreen g;
    private EstablishConnection dbController;

    Mechanics(GameScreen g, EstablishConnection dbController) {
        this.dbController = dbController;
        this.g = g;
        this.screenSize = g.settings.getSize();
        this.takt = g.settings.getSpeed();
        this.steps = 20;
        this.gameStarted = false;
        this.newGame();
    }

    public void newGame() {

        clearSnake(gameStarted);

        screenSize = g.settings.getSize(); // TODO: kann optimiert werden. Settings Objekt kann Variablen ersetzen
        takt = g.settings.getSpeed();
        g.backPanel.setMinimumSize(new Dimension(screenSize, screenSize));
        g.updateDisplayPlayerHighscore();
        g.frame.pack();

        eaten = false;
        this.snakeDirection = new Point(0, -steps);
        snake = new SnakeObject(screenSize / 2, steps);
        body = new ArrayList<SnakeObject>();
        body.add(snake);

        g.panel = new JPanel();
        g.panel.setBackground(Color.BLUE);
        g.panel.setBounds(snake.getPosition().x, snake.getPosition().y, g.bodySize, g.bodySize);
        g.snakeBody = new ArrayList<JPanel>();
        g.backPanel.add(g.panel);
        g.snakeBody.add(g.panel);

        g.frame.paintComponents(g.frame.getGraphics());
        creatEdable();
        gameStarted = true;
    }

    public void clearSnake(boolean gameStarted) {
        if (gameStarted) {
            for (JPanel p : g.snakeBody) {
                g.backPanel.remove(p);
            }
        }
    }

    public void moveSnake(boolean append) {
        Point temp = new Point(body.get(0).getPosition());

        if (snake.getPosition().x + snake.getDirection().x >= screenSize) {
            snake.setPostion(new Point(0 - snake.getDirection().x, snake.getPosition().y));
        } else if (snake.getPosition().x + snake.getDirection().x < 0) {
            snake.setPostion(new Point(screenSize, snake.getPosition().y));
        } else if (snake.getPosition().y + snake.getDirection().y >= screenSize) {
            snake.setPostion(new Point(snake.getPosition().x, 0 - snake.getDirection().y));
        } else if (snake.getPosition().y + snake.getDirection().y < 0) {
            snake.setPostion(new Point(snake.getPosition().x, screenSize));
        }

        body.get(0).movePosition();
        if (body.size() > 1) {
            for (int i = 1; i < body.size(); i++) {
                Point temp2 = new Point(body.get(i).getPosition());
                body.get(i).setPostion(temp);
                temp = temp2;
            }
        }
        if (append) {
            body.add(new SnakeObject(temp));
            g.addTiel(temp);
            this.eaten = false;
        }
        for (int i = 0; i < body.size(); i++) {
            g.snakeBody.get(i).setLocation(body.get(i).getPosition());
        }
    }

    public boolean collision() {
        for (int i = 1; i < g.snakeBody.size(); i++) {
            if (!g.panel.getBounds().intersection(g.snakeBody.get(i).getBounds()).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Point randomPoint(int minimum, int maximum) {
        Point zufall = new Point((int) (minimum + (maximum - minimum + 1) * Math.random()),
                (int) (minimum + (maximum - minimum + 1) * Math.random()));
        return zufall;
    }

    private void eatObject() {
        if (!g.panel.getBounds().intersection(g.food.getBounds()).isEmpty()) {
            boolean repeat = true;
            while (repeat) {
                Point zufall = randomPoint(20, screenSize - 20);
                g.food.setLocation(zufall);
                repeat = false;
                for (JPanel p : g.snakeBody) {
                    if (g.food.getBounds().intersects(p.getBounds())) {
                        repeat = true;
                    }
                }
            }
            this.eaten = true;
        }
    }

    public void creatEdable() {
        Point zufall = randomPoint(20, screenSize - 20);
        System.out.println("Food: " + zufall);
        g.addEdable(zufall);
    }

    public void setDirection(char dir) {
        if ('w' == dir && snake.getDirection().y != steps) {
            snakeDirection = (new Point(0, -steps));
        } else if ('a' == dir && snake.getDirection().x != steps) {
            snakeDirection = (new Point(-steps, 0));
        } else if ('s' == dir && snake.getDirection().y != -steps) {
            snakeDirection = (new Point(0, steps));
        } else if ('d' == dir && snake.getDirection().x != -steps) {
            snakeDirection = (new Point(steps, 0));
        } else if ('e' == dir) {
            this.eaten = true;
        }
    }

    public void setPoints() {
        int points = body.size() * 400 / takt;
        g.length.setText("Länge: " + Integer.toString(body.size()));
        g.points.setText("Punkte: " + Integer.toString(points));
        dbController.setPlayerLength(body.size());
        dbController.setPlayerPoints(points);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(takt);
            } catch (Exception e) {
                e.getStackTrace();
            }

            snake.setDirection(snakeDirection);
            moveSnake(eaten);
            setPoints();
            eatObject();
            if (collision()) {
                System.err.println("collision");
                System.err.println("Lost!");
                System.err.println("Länge: " + body.size());
                System.err.println("Punkte: " + g.points.getText());
                if (dbController.getPlayerPoints() > dbController.getPlayerHighscore()) {
                    dbController.sqlRequest(dbController.UPDATEPLAYERPOINTS);
                }
                newGame(); // Startet neues Spiel nach jeder Kollision. Für Spielewahl anpassen!!
                // break; // TODO: back to Startscreen
            }
            // System.out.println("Snake: "+ snake.getPosition());
        }
    }
}