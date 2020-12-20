package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {

    private static JLabel IDLabel;
    private static JTextField IDText;
    private static JLabel levelLabel;
    private static JTextField levelText;
    private static JButton button;
    private static int level;
    private static int id;
    private static boolean running = true;
    JFrame frame = new JFrame();

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public boolean isRunning() {
        return running;
    }


    public void init() {

        running = true;

        JPanel panel = new JPanel();
        frame = new JFrame();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Login");
        ImageIcon image = new ImageIcon("src\\Pokeball Logo.png");
        frame.setIconImage(image.getImage());
        frame.add(panel);

        panel.setLayout(null);

        IDLabel = new JLabel("ID");
        IDLabel.setBounds(10, 20, 80, 25);
        panel.add(IDLabel);

        IDText = new JTextField(20);
        IDText.setBounds(100, 20, 165, 25);
        panel.add(IDText);

        levelLabel = new JLabel("Level");
        levelLabel.setBounds(10, 50, 80, 25);
        panel.add(levelLabel);

        levelText = new JTextField(20);
        levelText.setBounds(100, 50, 165, 25);
        panel.add(levelText);

        button = new JButton("Start");
        button.setBounds(220, 110, 80, 25);
        button.setFocusable(false);
        button.addActionListener(new GUI());
        panel.add(button);


        JLabel message = new JLabel("Enter valid level number [0-23]");
        message.setBounds(10, 110, 240, 25);
        panel.add(message);


        frame.setVisible(true);


    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {

        String stringLevel = levelText.getText();

        try {
            level = Integer.parseInt(stringLevel);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String stringID = IDText.getText();

        try {
            id = Integer.parseInt(stringID);
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        System.out.println("Game starting");
        this.running = false;


    }
}
