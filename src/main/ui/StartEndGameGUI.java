package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//GUI for starting screen and ending screen
public class StartEndGameGUI extends JFrame {

    // Image from https://www.pikpng.com/pngvi/bmbwoo_blue-flame-boss-pixel-art-enemy-png-clipart/
    private static final ImageIcon startScreenImage = new ImageIcon("./data/PikPng.comBigImage.png");
    JPanel endPanel;

    // EFFECTS: Initialize and create GUI
    public StartEndGameGUI(boolean gameWon, boolean gameLost) {
        super("The Video Game");
        initializeGUI();
        createPanels(gameWon, gameLost);
        createButtonPanel();
        add(endPanel);
        setVisible(true);
    }

    // EFFECTS: Creates the window for GUI using BorderLayout
    public void initializeGUI() {
        setLayout(new BorderLayout());
        setSize(450, 560);
        createWindowListener();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: Creates the center main panel within JFrame depending on if game is Won, Lost, or neither
    public void createPanels(boolean gameWon, boolean gameLost) {
        endPanel = new JPanel(new BorderLayout());
        JPanel text = new JPanel();
        if (gameWon) {
            setSize(new Dimension(300, 150));
            setLocationRelativeTo(null);
            text.add(new JLabel("YOU WON!!!!!!!"));
            text.setBackground(Color.cyan);
        } else if (gameLost) {
            setSize(new Dimension(300, 150));
            setLocationRelativeTo(null);
            text.add(new JLabel("YOU LOST!!!"));
            text.setBackground(Color.RED);
        } else {
            text.add(new JLabel(startScreenImage));
        }
        endPanel.add(text, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: Creates a south JPanel consisting of restart and loadButton within main JFrame
    public void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        JButton restartButton = new JButton("PLAY NEW GAME");

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameGUI(false);
            }
        });
        buttonPanel.add(restartButton);

        JButton loadButton = new JButton("Load Game");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameGUI(true);
            }
        });
        buttonPanel.add(loadButton);
        endPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Influenced from AlarmSystem
    // EFFECTS: prints out eventlog when window closes
    public void createWindowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                for (Event event: EventLog.getInstance()) {
                    System.out.println(event.toString());
                }
            }
        });
    }
}
