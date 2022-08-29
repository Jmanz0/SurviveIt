package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    Player testPlayer;

    @BeforeEach
    void setup() {
        testPlayer = new Player();
    }

    @Test
    void testConstructor() {
        assertEquals(Player.STARTINGDAMAGE, testPlayer.getDamage());
        assertEquals(Player.STARTINGHEALTH, testPlayer.getHealth());
    }

    @Test
    void testConstructorWithParameters() {
        Player testPlayer1 = new Player(250, 150);
        assertEquals(150, testPlayer1.getDamage());
        assertEquals(250, testPlayer1.getHealth());
    }
}
