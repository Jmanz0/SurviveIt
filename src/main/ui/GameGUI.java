package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// GameGUI initializes and updates the GUI of current gameBoard
public class GameGUI extends JFrame {
    private static final String JSON_STORE = "./data/game.json";
    private static final String JSON_LOCATION = "./data/game.json";
    private static final Integer HEIGHT = 800;
    private static final Integer WIDTH = 1000;
    // Image from https://www.pikpng.com/pngvi/bmbwoo_blue-flame-boss-pixel-art-enemy-png-clipart/
    private static final ImageIcon enemyImage = new ImageIcon("./data/PikPng.com_blue-flame-png_840679 (2).png");
    // Image from https://imgbin.com/png/kSRJARxP/minecraft-mojang-video-game-pixel-art-png
    private static final ImageIcon playerImage = new ImageIcon("./data/minecraft-character.png");

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private GameBoard game;

    private JPanel notificationPanel;
    private JPanel enemies;
    private JPanel players;
    private JPanel board;
    private JPanel stats;
    JButton restartButton;
    JButton saveButton;
    JButton loadButton;
    JButton dodgeButton;
    JButton attackButton;
    JButton buyButton;

    // Method based off JsonSerializationDemo-master workRoomApp
    // Inspiration from SimpleDrawingPlayer
    // EFFECTS: runs game and initializes graphics depending on if we want to load last game
    public GameGUI(boolean loadLastGame) {
        super("The Video Game");
        jsonWriter = new JsonWriter(JSON_LOCATION);
        jsonReader = new JsonReader(JSON_LOCATION);
        initializeGraphics();
        game = new GameBoard();
        if (loadLastGame) {
            loadGame();
        }
        moveGameForward();
    }

    // EFFECTS: Initializes the window with graphics
    public void initializeGraphics() {
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        createWindowListener();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        generateBoardPanel();
        createButtons();
        setVisible(true);
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

    // Influenced from Traffic Light C3 in class lecture and SimpleDrawingPlayer
    // MODIFIES: this
    // EFFECTS: Creates the button panel with user story actions
    public void createButtons() {
        restartButton = new JButton("Restart");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        dodgeButton = new JButton("Dodge");
        attackButton = new JButton("Attack");
        buyButton = new JButton("Add another player for $" + GameBoard.COSTFORPLAYER);

        createLoadActionListeners();
        createGameBoardActionListeners();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.setSize(new Dimension(0, 0));
        add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(restartButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(dodgeButton);
        buttonPanel.add(attackButton);
        buttonPanel.add(buyButton);
    }

    // MODIFIES: this
    // EFFECTS: Creates action listeners for saving, loading, and restart button
    private void createLoadActionListeners() {
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
                updateVisuals();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame();
                updateVisuals();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(notificationPanel);
                doRestart();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Creates action listeners for gameboard buttons
    private void createGameBoardActionListeners() {
        dodgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(notificationPanel);
                doDodge();
                moveGameForward();
            }
        });

        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(notificationPanel);
                doAttack();
                moveGameForward();
            }
        });

        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(notificationPanel);
                doBuyPlayer();
                updateVisuals();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: creates the main board panel for stats, enemies and players
    private void generateBoardPanel() {
        board = new JPanel(new BorderLayout());

        Color backgroundColor = new Color(86, 125, 70);

        stats = new JPanel();
        stats.setBackground(Color.lightGray);
        board.add(stats, BorderLayout.NORTH);

        enemies = new JPanel();
        enemies.setBackground(backgroundColor);
        board.add(enemies, BorderLayout.CENTER);

        players = new JPanel();
        players.setBackground(backgroundColor);
        board.add(players, BorderLayout.SOUTH);

        notificationPanel = new JPanel();

        add(board);
    }

    // MODIFIES: this
    // EFFECTS: Updates gameBoard visuals by updating enemies, players and stats
    private void updateVisuals() {
        enemies.removeAll();
        players.removeAll();
        stats.removeAll();
        for (int i = 0; i < game.getGameEnemiesSize(); i++) {
            JLabel tempLabel = new JLabel();
            tempLabel.setIcon(enemyImage);
            enemies.add(tempLabel, BorderLayout.SOUTH);
        }

        for (int i = 0; i < game.getGamePlayersSize(); i++) {
            JLabel tempPlayerLabel = new JLabel();
            tempPlayerLabel.setIcon(playerImage);
            players.add(tempPlayerLabel, BorderLayout.SOUTH);
        }

        updateStats();
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: Updates the stats of gameBoard
    private void updateStats() {
        stats.add(new JLabel(" CASH: $" + game.getCoins() + " "), BorderLayout.NORTH);
        stats.add(new JLabel(" Player health: " + game.getPlayerTotalHealth() + " "), BorderLayout.NORTH);
        stats.add(new JLabel(" You did " + game.didDamage() + " damage "), BorderLayout.NORTH);
        stats.add(new JLabel(" Enemies: " + game.getGameEnemiesSize() + " "), BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: moves game forward by updating
    private void moveGameForward() {
        game.maybeAddEnemies();
        updateVisuals();

        if (game.isGameOver()) {
            endGame();
        }
    }

    // REQUIRES: Not 0 player health, or all enemies defeated in game
    // MODIFIES: this
    // EFFECTS: adds another player to players and create notification
    private void doBuyPlayer() {
        if (game.buyPlayer()) {
            updateNotificationPanel("Bought 1 Player", Color.GRAY);
        } else {
            updateNotificationPanel("Insufficient Funds!", Color.RED);
        }
    }

    // MODIFIES: this
    // EFFECTS: Attacks first enemy within enemies with total player damage, and create notification if defeated enemy
    private void doAttack() {
        if (game.attackEnemies()) {
            updateNotificationPanel("DEFEATED AN ENEMY! +$" + GameBoard.COINREWARD, Color.GRAY);
        }
        game.decreaseEnemiesMovesToAttack();
        game.damagePlayers(game.calculateEnemiesTotalDamage());
    }

    // MODIFIES: this
    // EFFECTS: initializes a new game, and disposes of current JFrame
    private void doRestart() {
        dispose();
        new GameGUI(false);
    }

    // REQUIRES: Not 0 player health, or all enemies defeated in game
    // MODIFIES: this
    // EFFECTS: Decreases enemies moves to attack and displays stats and create notification
    private void doDodge() {
        updateVisuals();
        updateNotificationPanel("Woosh!", Color.GRAY);
        game.dodge();
    }


    // MODIFIES: this
    // EFFECTS: end game by disposing current JFrame and start new StartEndGameGUI
    private void endGame() {
        dispose();
        new StartEndGameGUI(game.gameWon(), game.gameLost());
    }

    // Based off JsonSerializationDemo-Master
    // EFFECTS: saves the workroom to file, and creates notification
    private void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            updateNotificationPanel("Saved game to " + JSON_STORE, Color.BLUE);
        } catch (FileNotFoundException e) {
            updateNotificationPanel("Unable to write to file: " + JSON_STORE, Color.BLUE);
        }
    }

    // Based off JsonSerializationDemo-Master
    // MODIFIES: this
    // EFFECTS: loads game from file, and creates notification
    private void loadGame() {
        try {
            game = jsonReader.read();
            updateNotificationPanel("Loaded game from " + JSON_STORE, Color.BLUE);
        } catch (IOException e) {
            updateNotificationPanel("Unable to read from file:" + JSON_STORE, Color.BLUE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes previous items in notificationPanel, updates it with new message and backgroundColor, and
    //          add to North JFrame
    private void updateNotificationPanel(String message, Color backgroundColor) {
        notificationPanel.removeAll();
        notificationPanel.add(new JLabel(message));
        notificationPanel.setBackground(backgroundColor);
        add(notificationPanel, BorderLayout.NORTH);
    }
}

