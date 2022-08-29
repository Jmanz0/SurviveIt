package model;

import org.json.JSONObject;
import persistence.Writable;

// A player with specific health and damage
public class Player extends Being implements Writable {
    public static final int STARTINGHEALTH = 1250;
    public static final int STARTINGDAMAGE = 100;

    // EFFECTS: create a player with STARTINGHEALTH and STARTINGDAMAGE
    public Player() {
        health = STARTINGHEALTH;
        damage = STARTINGDAMAGE;
    }

    // EFFECTS: creates a player with health h and damage d
    public Player(int h, int d) {
        health = h;
        damage = d;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("health", health);
        json.put("damage", damage);
        return json;
    }

}
