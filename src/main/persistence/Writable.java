package persistence;

import org.json.JSONObject;

public interface Writable {
    // Structure from JsonSerializationDemo-master
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
