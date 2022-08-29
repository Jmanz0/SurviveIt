package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    Enemy testEnemy;

    @BeforeEach
    void setup() {
        testEnemy = new Enemy();
    }

    @Test
    void testConstructor() {
        assertEquals(Enemy.MOVESTOATTACK, testEnemy.getMovesLeftToAttack());
        assertEquals(Enemy.STARTINGDAMAGE, testEnemy.getDamage());
        assertEquals(Enemy.STARTINGHEALTH, testEnemy.getHealth());
    }

    @Test
    void testConstructorWithParameters() {
        Enemy testEnemy1 = new Enemy(200, 300, 2);
        assertEquals(2, testEnemy1.getMovesLeftToAttack());
        assertEquals(300, testEnemy1.getDamage());
        assertEquals(200, testEnemy1.getHealth());
    }

    @Test
    void testDecreaseMovesToAttack() {
        testEnemy.decreaseMovesToAttack();
        assertEquals(Enemy.MOVESTOATTACK - 1, testEnemy.getMovesLeftToAttack());
    }
}