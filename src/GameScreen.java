import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class GameScreen implements ActionListener, KeyListener {

    JFrame frame;
    JPanel panel, backPanel, food;
    JMenuBar menueBar;
    JMenu menu, speedMenu, sizeMenu;
    JMenuItem newGame;
    JRadioButton  speedSlow, speedFast, speedCrazy, speedAdaptive, sizeSmal, sizeMid, sizeLarge, sizeXtra;
    JLabel time, points, length;        // TODO: Zeit, Punkte, Länge
    
    Mechanics mechanics;
    int bodySize, edabelSize, settingSize, settingSpeed;
    ArrayList<JPanel> snakeBody;
    int screenSize;
    Settings settings;

    public GameScreen()
    {   
        this.bodySize = 20;
        this.edabelSize = 10;
        this.settings = new Settings(400, 20, bodySize);
        this.screenSize = settings.getSize();

        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);

        points = new JLabel("Punkte: 0");
        length = new JLabel("Länge: 0");
        JPanel gameStats = new JPanel();
        JPanel speratorPanel = new JPanel();
        gameStats.add(points);
        gameStats.add(speratorPanel);
        gameStats.add(length);

        menueBar = new JMenuBar();
        menu = new JMenu("GameSettings");
        newGame = new JMenuItem("new Game");
        speedMenu = new JMenu("Speed");
        sizeMenu = new JMenu("Size");
        speedAdaptive = new JRadioButton ("TBD");
        speedCrazy = new JRadioButton ("Crazy (Quadrupel Points)");
        speedFast = new JRadioButton ("Fast (Double Points)");
        speedSlow = new JRadioButton ("Slow (Single Points)", true);
        sizeXtra = new JRadioButton ("50x50");
        sizeLarge = new JRadioButton ("30x30");
        sizeMid = new JRadioButton ("20x20", true);
        sizeSmal = new JRadioButton ("10x10");
        
        ButtonGroup speedGroup = new ButtonGroup();
        speedGroup.add(speedAdaptive);                  // TODO: kontinuierliche Geschwindigkeitssteigerung je nach Spieldauer
        speedGroup.add(speedCrazy);
        speedGroup.add(speedFast);
        speedGroup.add(speedSlow);

        ButtonGroup sizeGroup = new ButtonGroup();      //TODO: automatische Fensteranpassung bei Speilfeld wechsel
        sizeGroup.add(sizeXtra);
        sizeGroup.add(sizeLarge);
        sizeGroup.add(sizeMid);
        sizeGroup.add(sizeSmal);

        newGame.addActionListener(this);
        speedAdaptive.addActionListener(this);
        speedCrazy.addActionListener(this);
        speedFast.addActionListener(this);
        speedSlow.addActionListener(this);
        sizeLarge.addActionListener(this);
        sizeMid.addActionListener(this);
        sizeSmal.addActionListener(this);
        sizeXtra.addActionListener(this);
        
        speedMenu.add(speedAdaptive);speedMenu.add(speedCrazy);speedMenu.add(speedFast);speedMenu.add(speedSlow);
        sizeMenu.add(sizeXtra);sizeMenu.add(sizeLarge);sizeMenu.add(sizeMid);sizeMenu.add(sizeSmal);
        menu.add(newGame);
        menu.addSeparator();
        menu.add(speedMenu);
        menu.add(sizeMenu);
        menueBar.add(menu);
        menueBar.add(gameStats);
        frame.setJMenuBar(menueBar);
        frame.setVisible(true);
        
        backPanel = new JPanel();
        backPanel.setBackground(Color.darkGray);
        backPanel.setLayout(null);
        frame.add(backPanel);
        backPanel.setPreferredSize(new Dimension(screenSize,screenSize));
        frame.pack();

        food = new JPanel();
        food.setBackground(Color.RED);
        backPanel.add(food);

        mechanics = new Mechanics(this);
        Thread thread = new Thread(mechanics);
        thread.start();
    }
    
    public void addTiel(Point p) {
        JPanel tile = new JPanel();
        tile.setBackground(Color.GREEN);
        tile.setBounds(p.x, p.y, bodySize, bodySize);
        backPanel.add(tile);
        snakeBody.add(tile);
    }
    public void addEdable(Point p) 
    {
        food.setBounds(p.x, p.y, edabelSize, edabelSize);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        mechanics.setDirection(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        
        System.out.println(e);
        if(e.getSource().equals(newGame))
        {
            mechanics.newGame();
        }
        else if(e.getSource().equals(sizeXtra))
        {
            settings.setSize(50, this.bodySize);
        }
        else if(e.getSource().equals(sizeLarge))
        {
            settings.setSize(30, this.bodySize);
        }    
        else if(e.getSource().equals(sizeMid))
        {
            settings.setSize(20, this.bodySize);
        }
        else if(e.getSource().equals(sizeSmal))
        {
            settings.setSize(10, this.bodySize);
        }
        else if(e.getSource().equals(speedAdaptive))
        {
            this.settings.setSpeed(1000);
        }
        else if(e.getSource().equals(speedCrazy))
        {
            this.settings.setSpeed(50);
        }
        else if(e.getSource().equals(speedFast))
        {
            this.settings.setSpeed(100);
        }
        else if(e.getSource().equals(speedSlow))
        {
            this.settings.setSpeed(400);
        }

    }

}
