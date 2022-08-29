package persistence;

import model.Enemy;
import model.GameBoard;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Heavily influenced from JsonSerializationDemo-master JsonReader class
// Represents a reader that reads GameBoard from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads GameBoard from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GameBoard read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGameBoard(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses GameBoard from JSON object and returns it
    private GameBoard parseGameBoard(JSONObject jsonObject) {
        int coins = jsonObject.getInt("coins");
        boolean lastMoveWasAttack = jsonObject.getBoolean("lastMoveWasAttack");
        GameBoard g = new GameBoard(coins, lastMoveWasAttack);
        addGameEnemies(g, jsonObject);
        addGamePlayers(g, jsonObject);
        return g;
    }

    // MODIFIES: g
    // EFFECTS: parses GameEnemies from JSON object and adds them to GameBoard
    private void addGameEnemies(GameBoard g, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("gameEnemies");
        for (Object json : jsonArray) {
            JSONObject nextEnemy = (JSONObject) json;
            addEnemy(g, nextEnemy);
        }
    }

    // MODIFIES: g
    // EFFECTS: parses Enemy from JSON object and adds it to GameBoard
    private void addEnemy(GameBoard g, JSONObject jsonObject) {
        int health = jsonObject.getInt("health");
        int damage = jsonObject.getInt("damage");
        int movesLeft = jsonObject.getInt("movesLeftToAttack");
        Enemy enemy = new Enemy(health, damage, movesLeft);
        g.addEnemy(enemy);
    }

    // MODIFIES: g
    // EFFECTS: parses gamePlayers from JSON object and adds them to GameBoard
    private void addGamePlayers(GameBoard g, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("gamePlayers");
        for (Object json : jsonArray) {
            JSONObject nextPlayer = (JSONObject) json;
            addPlayer(g, nextPlayer);
        }
    }

    // MODIFIES: g
    // EFFECTS: parses Player from JSON object and adds it to GameBoard
    private void addPlayer(GameBoard g, JSONObject jsonObject) {
        int health = jsonObject.getInt("health");
        int damage = jsonObject.getInt("damage");
        Player player = new Player(health, damage);
        g.addPlayer(player);
    }
}
