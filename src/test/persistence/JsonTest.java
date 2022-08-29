package persistence;

import model.Enemy;
import model.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    // test structure from JsonSerializationDemo-master

    protected void checkPlayer(int health, int damage, Player player) {
        assertEquals(health, player.getHealth());
        assertEquals(damage, player.getDamage());
    }

    protected void checkEnemy(int health, int damage, int movesLeft, Enemy enemy) {
        assertEquals(health, enemy.getHealth());
        assertEquals(damage, enemy.getDamage());
        assertEquals(movesLeft, enemy.getMovesLeftToAttack());
    }
}
