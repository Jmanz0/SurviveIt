package persistence;

import model.Enemy;
import model.GameBoard;
import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    // test structure from JsonSerializationDemo-master

    @Test
    void testWriterInvalidFile() {
        try {
            GameBoard g = new GameBoard();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGameBoard() {
        try {
            GameBoard g = new GameBoard(0, false);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGameBoard.json");
            writer.open();
            writer.write(g);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyGameBoard.json");
            g = reader.read();
            assertEquals(0, g.getGamePlayersSize());
            assertEquals(0, g.getGameEnemiesSize());
            assertEquals(0, g.getCoins());
            assertFalse(g.isLastMoveWasAttack());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterNormalGameBoard() {
        try {
            GameBoard g = new GameBoard(20, true);
            g.addPlayer(new Player(376, 120));
            g.addEnemy(new Enemy(123, 60, 2));
            JsonWriter writer = new JsonWriter("./data/testWriterNormalGameBoard.json");
            writer.open();
            writer.write(g);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNormalGameBoard.json");
            g = reader.read();
            List<Enemy> enemies = g.getGameEnemies();
            List<Player> players = g.getGamePlayers();
            assertEquals(1, enemies.size());
            assertEquals(1, players.size());
            checkEnemy(123, 60, 2, enemies.get(0));
            checkPlayer(376, 120, players.get(0));
            assertEquals(20, g.getCoins());
            assertTrue(g.isLastMoveWasAttack());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}