package model;

// An abstract class for commonalities of Enemy and Player
public abstract class Being {
    protected Integer health;
    protected Integer damage;

    // REQUIRES: positive Integer (including 0)
    // MODIFIES: this
    // EFFECTS: reduce health of player
    public void takeDamage(int damageTaken) {
        health -= damageTaken;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getDamage() {
        return damage;
    }
}
