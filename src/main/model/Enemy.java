package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Random;

// An enemy with health, damage and movesLeftToAttack stats
public class Enemy extends Being implements Writable {
    public static final int MOVESTOATTACK = 3;
    public static final int STARTINGHEALTH = 120;
    public static final int STARTINGDAMAGE = 75;

    private Integer movesLeftToAttack;

    // EFFECTS: create an enemy with MOVESTOATTACK, STARTINGDAMAGE and STARTINGHEALTH
    public Enemy() {
        movesLeftToAttack = MOVESTOATTACK;
        health = STARTINGHEALTH;
        damage = STARTINGDAMAGE;
    }

    // Effects: creates an enemy with healh h, damage d and movesLeftToAttack mlta
    public Enemy(int h, int d, int mlta) {
        health = h;
        damage = d;
        movesLeftToAttack = mlta;
    }

    // MODIFIES: this
    // EFFECTS: reduce the movesLeftToAttack by one
    public void decreaseMovesToAttack() {
        movesLeftToAttack--;
    }


    // TODO: add enemy names, might want to make an abstract class for enemy

    public Integer getMovesLeftToAttack() {
        return movesLeftToAttack;
    }

    public void setMovesLeftToAttack(Integer movesLeftToAttack) {
        this.movesLeftToAttack = movesLeftToAttack;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("health", health);
        json.put("damage", damage);
        json.put("movesLeftToAttack", movesLeftToAttack);
        return json;
    }
}
