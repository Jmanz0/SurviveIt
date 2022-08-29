package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;


import java.util.ArrayList;
import java.util.List;

// Current state of the game
public class GameBoard implements Writable {
    public static final Integer STARTINGENEMYAMOUNT = 4;
    private static final Integer MAX = 1;
    private static final Integer MIN = 0;
    public static final Integer COSTFORPLAYER = 300;
    public static final Integer COINREWARD = 80;

    private List<Player> gamePlayers;
    private List<Enemy> gameEnemies;
    private int coins;
    private boolean lastMoveWasAttack;

    // EFFECTS: Initialize a new gameBoard with an empty list of gameEnemies, add STARTINGENEMYAMOUNT of new Enemy,
    //          an empty list of gamePlayers, 0 coins, set lastMoveWasAttack to false and 1 player.
    public GameBoard() {
        lastMoveWasAttack = false;
        gamePlayers = new ArrayList<>();
        gameEnemies = new ArrayList<>();
        gamePlayers.add(new Player());
        coins = 0;
        for (int x = 0; x < STARTINGENEMYAMOUNT; x++) {
            this.gameEnemies.add(new Enemy());
        }
    }

    // EFFECTS: Initialize a new gameBoard with an empty list of gameEnemies,
    //          an empty list of gamePlayers, c coins, and lmwa lastMoveWasAttack.
    public GameBoard(int c, boolean lmwa) {
        lastMoveWasAttack = lmwa;
        gamePlayers = new ArrayList<>();
        gameEnemies = new ArrayList<>();
        coins = c;
    }

    // MODIFIES: this
    // EFFECTS: Set lastMoveWasAttack to true, and if a enemy is destroyed add COINREWARD. Return if
    //          the enemy was destroyed.
    public boolean attackEnemies() {
        boolean isEnemyDestroyed = damageEnemies(getPlayerTotalDamage());
        if (isEnemyDestroyed) {
            coins += COINREWARD;
        }
        lastMoveWasAttack = true;
        return isEnemyDestroyed;
    }

    // MODIFIES: this
    // EFFECTS: If users' coins is sufficient for COSTFORPLAYER, then add a player to gamePlayers,
    //         subtract COSTFORPLAYER, set lastMoveWasAttack to false, and return true; otherwise false
    //          also add new event when a player is added
    public boolean buyPlayer() {
        if (coins >= COSTFORPLAYER) {
            gamePlayers.add(new Player());
            coins -= COSTFORPLAYER;
            lastMoveWasAttack = false;
            EventLog.getInstance().logEvent(new Event("Added player"));
            return true;
        }
        return false;
    }

    // EFFECTS: return if GameLost() or gameWon() is true
    public boolean isGameOver() {
        return gameLost() || gameWon();
    }

    // MODIFIES: this
    // EFFECTS: 50% chance of adding a new enemy to game
    public void maybeAddEnemies() {
        int x = 0;
        if (Math.floor(Math.random() * (MAX - MIN + 1) + MIN) == 1) {
            gameEnemies.add(new Enemy());
        }
    }

    // MODIFIES: this
    // EFFECTS: decrease gameEnemies moves to attack and set lastMoveWasAttack to false
    public void dodge() {
        decreaseEnemiesMovesToAttack();
        lastMoveWasAttack = false;
    }

    // EFFECTS: returns players total damage, if lastMoveWasAttack is true
    public int didDamage() {
        if (lastMoveWasAttack) {
            return getPlayerTotalDamage();
        } else {
            return 0;
        }
    }

    // EFFECTS: adds total damage of all player in players
    public int getPlayerTotalDamage() {
        int totalDamage = 0;
        for (Player p : gamePlayers) {
            totalDamage += p.getDamage();
        }
        return totalDamage;
    }

    // EFFECTS: adds total health of all players in players
    public int getPlayerTotalHealth() {
        int totalHealth = 0;
        for (Player p : gamePlayers) {
            totalHealth += p.getHealth();
        }
        return totalHealth;
    }

    // MODIFIES: this and Player
    // EFFECTS: damages first player within list, if damage is greater than player, remove that player from player
    public void damagePlayers(int enemyDamage) {
        if (gamePlayers.get(0).getHealth() <= enemyDamage) {
            gamePlayers.remove(0);
        } else {
            gamePlayers.get(0).takeDamage(enemyDamage);
        }
    }

    // MODIFIES: Enemy e
    // EFFECTS: calculate and return total damage of enemies
    public Integer calculateEnemiesTotalDamage() {
        int totalDamage = 0;
        for (Enemy e : gameEnemies) {
            if (e.getMovesLeftToAttack() == 0) {
                totalDamage += e.getDamage();
                e.setMovesLeftToAttack(Enemy.MOVESTOATTACK);
            }
        }
        return totalDamage;
    }

    // MODIFIES: player
    // EFFECTS: decrease movesLeftToAttack for all player in players
    public void decreaseEnemiesMovesToAttack() {
        for (Enemy e : gameEnemies) {
            e.decreaseMovesToAttack();
        }
    }

    // MODIFIES: this and Enemy
    // EFFECTS: damages first Enemy within list, if damage is greater than Enemy, remove that Enemy from enemies
    //          also add new event when enemy is defeated
    public boolean damageEnemies(int playerDamage) {
        if (gameEnemies.get(0).getHealth() <= playerDamage) {
            gameEnemies.remove(0);
            EventLog.getInstance().logEvent(new Event("Defeated enemy"));
            return true;
        } else {
            gameEnemies.get(0).takeDamage(playerDamage);
            return false;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("coins", coins);
        json.put("lastMoveWasAttack", lastMoveWasAttack);
        json.put("gamePlayers", gamePlayersToJson());
        json.put("gameEnemies", gameEnemiesToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray gamePlayersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Player p : gamePlayers) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray gameEnemiesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Enemy e : gameEnemies) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: adds a player to gamePlayers
    public void addPlayer(Player player) {
        gamePlayers.add(player);
    }

    // MODIFIES: this
    // EFFECTS: adds an enemy to gameEnemies
    public void addEnemy(Enemy enemy) {
        gameEnemies.add(enemy);
    }

    //EFFECTS: returns true if no players remain; otherwise false
    public boolean gameLost() {
        return gamePlayers.size() == 0;
    }

    //EFFECTS: returns true if there is no enemies left; otherwise false
    public boolean gameWon() {
        return gameEnemies.size() == 0;
    }

    public List<Player> getGamePlayers() {
        return gamePlayers;
    }

    public int getCoins() {
        return coins;
    }

    public int getGamePlayersSize() {
        return gamePlayers.size();
    }

    public int getGameEnemiesSize() {
        return gameEnemies.size();
    }

    public Enemy getEnemyIndex(int number) {
        return gameEnemies.get(number);
    }

    public void setGamePlayers(List<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void setGameEnemies(List<Enemy> gameEnemies) {
        this.gameEnemies = gameEnemies;
    }

    public boolean isLastMoveWasAttack() {
        return lastMoveWasAttack;
    }

    public List<Enemy> getGameEnemies() {
        return gameEnemies;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
