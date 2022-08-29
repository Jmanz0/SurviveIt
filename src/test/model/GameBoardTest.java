package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    GameBoard testGame;

    @BeforeEach
    void beforeEach() {
        testGame = new GameBoard();
    }

    @Test
    void testGameBoard() {
        assertFalse(testGame.isLastMoveWasAttack());
        assertEquals(1, testGame.getGamePlayersSize());
        assertEquals(GameBoard.STARTINGENEMYAMOUNT, testGame.getGameEnemiesSize());
        assertEquals(0, testGame.getCoins());
    }

    @Test
    void testGameBoardWithTwoParameters() {
        GameBoard testGame2 = new GameBoard(26, true);
        assertTrue(testGame2.isLastMoveWasAttack());
        assertEquals(0, testGame2.getGamePlayersSize());
        assertEquals(0, testGame2.getGameEnemiesSize());
        assertEquals(26, testGame2.getCoins());
    }

    @Test
    void testAttackEnemiesDestroysEnemy() {
        testGame.attackEnemies();
        while(testGame.getPlayerTotalDamage() <= Enemy.STARTINGHEALTH) {
            testGame.addPlayer(new Player());
        }
        boolean bool = testGame.attackEnemies();
        assertTrue(bool);
        assertEquals(GameBoard.COINREWARD, testGame.getCoins());
        assertTrue(testGame.isLastMoveWasAttack());
    }

    @Test
    void testAttackEnemiesDoesntDestroysEnemy() {
        boolean bool = testGame.attackEnemies();
        assertFalse(bool);
        assertEquals(0, testGame.getCoins());
        assertTrue(testGame.isLastMoveWasAttack());
    }

    @Test
    void testBuyPlayerSufficientFunds() {
        testGame.setCoins(GameBoard.COSTFORPLAYER);
        boolean bool = testGame.buyPlayer();
        assertEquals(2, testGame.getGamePlayersSize());
        assertTrue(bool);
    }

    @Test
    void testBuyPlayerInsufficientFunds() {
        testGame.setCoins(GameBoard.COSTFORPLAYER - 5);
        boolean bool = testGame.buyPlayer();
        assertEquals(1, testGame.getGamePlayersSize());
        assertFalse(bool);
    }

    @Test
    void testIsGameOverGameLost() {
        testGame.setGameEnemies(new ArrayList<>());
        assertTrue(testGame.isGameOver());
    }

    @Test
    void testIsGameOverGameWon() {
        testGame.setGamePlayers(new ArrayList<>());
        assertTrue(testGame.isGameOver());

    }

    @Test
    void testIsGameOverFalse() {
        assertFalse(testGame.isGameOver());
    }

    @Test
    // Test influenced from coin flip project
    public void testMaybeAddEnemies(){
        int timesChanged = 0;
        int lastStatus = testGame.getGameEnemiesSize();
        for (int i=0; i<10; i++){
            testGame.maybeAddEnemies();
            int status = testGame.getGameEnemiesSize();
            if (!(status == lastStatus)){
                timesChanged++;
                lastStatus = status;
            }
        }
        assertFalse(timesChanged==0);
    }


    //Test influenced by coin flip project
    @Test
    public void testMaybeAddEnemiesRandomness(){
        int lastStatus = testGame.getGameEnemiesSize();
        int longestRunOfSameAnswer = 0;
        int currentRun = 0;
        for (int i=0; i<100; i++){
            testGame.maybeAddEnemies();
            int status = testGame.getGameEnemiesSize();
            if (status == lastStatus){
                currentRun++;
                if (currentRun > longestRunOfSameAnswer){
                    longestRunOfSameAnswer = currentRun;
                }
            }
            else {
                lastStatus = status;
                currentRun = 0;
            }
        }
        assertFalse(longestRunOfSameAnswer>10);
    }

    @Test
    void testDodge() {
        int totalHealth = testGame.getPlayerTotalHealth();
        testGame.dodge();
        assertEquals(totalHealth, testGame.getPlayerTotalHealth());
        assertFalse(testGame.isLastMoveWasAttack());

        for (Enemy enemy: testGame.getGameEnemies()) {
            assertEquals(Enemy.MOVESTOATTACK - 1, enemy.getMovesLeftToAttack());
        }
    }

    @Test
    void testDidDamageNoDamage() {
        int didDamage = testGame.didDamage();
        assertEquals(0, didDamage);
    }

    @Test
    void testDidDamageDamage() {
        testGame.attackEnemies();
        int didDamage = testGame.didDamage();
        assertEquals(testGame.getPlayerTotalDamage(), didDamage);
    }

    @Test
    void testGetPlayerTotalDamage() {
        testGame.addPlayer(new Player());
        assertEquals(Player.STARTINGDAMAGE * 2, testGame.getPlayerTotalDamage());

    }

    @Test
    void testGetPlayerTotalHealth() {
        testGame.addPlayer(new Player());
        assertEquals(Player.STARTINGHEALTH * 2, testGame.getPlayerTotalHealth());
    }

    @Test
    void testDamagePlayersAboveHealth() {
        testGame.damagePlayers(Player.STARTINGHEALTH + 10);
        assertEquals(0, testGame.getGamePlayersSize());
    }

    @Test
    void testDamagePlayersAtHealth() {
        testGame.damagePlayers(Player.STARTINGHEALTH);
        assertEquals(0, testGame.getGamePlayersSize());
    }

    @Test
    void testDamagePlayersBelowHealth() {
        testGame.damagePlayers(Player.STARTINGHEALTH - 1);
        assertEquals(1, testGame.getGamePlayersSize());
    }

    @Test
    void testCalculateEnemiesTotalDamage() {
        assertEquals(0, testGame.calculateEnemiesTotalDamage());

        for (int x = 0; x < Enemy.MOVESTOATTACK; x++) {
            testGame.decreaseEnemiesMovesToAttack();
        }
        assertEquals(Enemy.STARTINGDAMAGE * GameBoard.STARTINGENEMYAMOUNT, testGame.calculateEnemiesTotalDamage());
    }

    @Test
    void testDecreaseEnemiesMovesToAttack() {
        testGame.decreaseEnemiesMovesToAttack();
        int index = 0;
        for (Enemy enemy: testGame.getGameEnemies()) {
            assertEquals(Enemy.MOVESTOATTACK - 1, testGame.getEnemyIndex(index).getMovesLeftToAttack());
            index++;
        }
    }

    @Test
    void testDamageEnemiesAboveHealth() {
        testGame.damageEnemies(Enemy.STARTINGHEALTH + 5);
        assertEquals(GameBoard.STARTINGENEMYAMOUNT - 1, testGame.getGameEnemiesSize());

    }

    @Test
    void testDamageEnemiesAtHealth() {
        testGame.damageEnemies(Enemy.STARTINGHEALTH);
        assertEquals(GameBoard.STARTINGENEMYAMOUNT - 1, testGame.getGameEnemiesSize());
    }

    @Test
    void testDamageEnemiesBelowHealth() {
        testGame.damageEnemies(Enemy.STARTINGHEALTH - 1);
        assertEquals(GameBoard.STARTINGENEMYAMOUNT, testGame.getGameEnemiesSize());
    }

    @Test
    void testGameLost() {
        assertFalse(testGame.gameLost());
        testGame.setGamePlayers(new ArrayList<>());
        assertTrue(testGame.gameLost());
    }

    @Test
    void testGameWon() {
        assertFalse(testGame.gameWon());
        testGame.setGameEnemies(new ArrayList<>());
        assertTrue(testGame.gameWon());
    }

    @Test
    void testAddPlayer() {
        Player player = new Player();
        testGame.addPlayer(player);
        assertEquals(2, testGame.getGamePlayersSize());
        assertTrue(testGame.getGamePlayers().get(1).equals(player));
    }

    @Test
    void testAddEnemy() {
        Enemy enemy = new Enemy();
        testGame.addEnemy(enemy);
        assertEquals(GameBoard.STARTINGENEMYAMOUNT + 1, testGame.getGameEnemiesSize());
        assertTrue(testGame.getGameEnemies().get(GameBoard.STARTINGENEMYAMOUNT).equals(enemy));
    }
}