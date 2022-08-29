package persistence;

import model.Enemy;
import model.GameBoard;
import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {
    // test structure from JsonSerializationDemo-master

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/wrongFile.json");
        try {
            GameBoard g = reader.read();
            fail("expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyGameBoard() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyGameBoard.json");
        try {
            GameBoard g = reader.read();
            assertEquals(0, g.getGamePlayersSize());
            assertEquals(0, g.getGameEnemiesSize());
            assertEquals(0, g.getCoins());
            assertFalse(g.isLastMoveWasAttack());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGameBoard() {
        JsonReader reader = new JsonReader("./data/testReaderNormalGameBoard.json");
        try {
            GameBoard g = reader.read();
            List<Enemy> enemies = g.getGameEnemies();
            List<Player> players = g.getGamePlayers();
            assertEquals(3, enemies.size());
            assertEquals(2, players.size());
            checkEnemy(40, 75, 3, enemies.get(0));
            checkEnemy(120, 75, 2, enemies.get(1));
            checkEnemy(120, 75, 1, enemies.get(2));
            checkPlayer(200, 100, players.get(0));
            checkPlayer(1250, 100, players.get(1));
            assertEquals(80, g.getCoins());
            assertTrue(g.isLastMoveWasAttack());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}