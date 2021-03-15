import database.EstablishConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

public class LoginScreen implements ActionListener {
    private JFrame frame;
    private JTextField passwordTextField;
    private JTextField nameTextField;
    private EstablishConnection dbController;

    public LoginScreen(EstablishConnection establishConnection) {

        this.dbController = establishConnection;
        int width = 250;
        int height = 250;
        frame = new JFrame("Loginscreen");
        frame.setLocation(900, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(Color.lightGray);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel loginText = new JLabel("LOGIN");
        loginText.setFont(new Font(null, Font.BOLD, 18));
        loginText.setForeground(Color.LIGHT_GRAY);
        loginText.setBounds(100, 20, 60, 40);
        loginText.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(loginText);

        JLabel name = new JLabel("Name");
        name.setBounds(40, 80, 100, 30);
        name.setForeground(Color.LIGHT_GRAY);
        panel.add(name);

        JLabel password = new JLabel("Password");
        password.setBounds(40, 130, 100, 30);
        password.setForeground(Color.LIGHT_GRAY);
        panel.add(password);

        nameTextField = new JTextField();
        nameTextField.setBounds(250 - 40 - 100, 80, 100, 30);
        panel.add(nameTextField);

        passwordTextField = new JTextField();
        passwordTextField.setBounds(250 - 40 - 100, 130, 100, 30);
        panel.add(passwordTextField);

        JButton okButton = new JButton("Einlogen...");
        okButton.addActionListener(this);
        okButton.setBounds(125 - 75, 200, 150, 30);
        panel.add(okButton);

        panel.setLayout(null);
        frame.setContentPane(panel);
        frame.pack();
    }

    public boolean newPlayerFrame() {

        int choice = JOptionPane.showConfirmDialog(frame, "Player not found. Create?", "New Player?",
                JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    public void startNewGame() {
        new GameScreen(dbController);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Einlogen...")) {

            if ("".equals(nameTextField.getText()) || "".equals(passwordTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Please fill all form fields!", "Missing Information",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {

                dbController.setPlayerName(nameTextField.getText());
                dbController.setPlayerPassword(passwordTextField.getText());

                if (this.dbController.sqlRequest(dbController.QUERYPLAYER)) {
                    this.dbController.sqlRequest(dbController.QUERYPLAYERPOINTS);
                    frame.dispose();
                    startNewGame();
                } else {
                    if (newPlayerFrame()) {
                        dbController.sqlRequest(dbController.ADDPLAYER);
                        frame.dispose();
                        startNewGame();
                    } else {
                        System.out.println("repeat");
                    }
                }
            }
        }
    }
}
