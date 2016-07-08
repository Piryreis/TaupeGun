package fr.valentin.taupegun.taupe;

import org.bukkit.entity.Player;

/**
 * Created by Valentin on 24/08/2015.
 */
public class Taupe {

    private Player player;
    private String name;
    private int id;
    public boolean isRevelated = false;

    public Taupe(Player player, int id){
        this.player = player;
        this.id = id;

        this.name = player.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
