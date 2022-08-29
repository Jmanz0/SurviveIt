package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeingTest {
    Enemy testEnemy;

    @Test
    void takeDamage() {
        testEnemy = new Enemy();
        testEnemy.takeDamage(150);
        assertEquals(Enemy.STARTINGHEALTH - 150, testEnemy.getHealth());
    }

}