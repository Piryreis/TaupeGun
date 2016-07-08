package fr.valentin.taupegun.teams;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.taupe.Taupe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

/**
 * Created by Valentin on 10/07/2015.
 */
public class TGTeam {

    private String name;
    private String displayName;
    private ChatColor color;
    private ArrayList<Player> players;
    private ArrayList<String> offlinePlayers;
    private Taupe taupe = null;
    public Team getScoreboardTeam;

    private TaupeGun plugin = TaupeGun.getInstance();

    public TGTeam(String name, String displayName, ChatColor color){
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.players = new ArrayList<Player>();
        this.offlinePlayers = new ArrayList<String>();
    }

    public String getName(){
        return name;
    }

    public ChatColor getColor(){
        return color;
    }

    public String getDisplayName(){
        return displayName;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public ArrayList<String> getOfflinePlayers(){
        return offlinePlayers;
    }

    public Taupe getTaupe(){
        return taupe;
    }

    public void setDisplayName(String newDisplayName){
        displayName = newDisplayName;
        fr.valentin.taupegun.scoreboard.Team.getInstance().setDisplayName(this);
        this.reloadPlayersTags();
    }

    public void addPlayer(Player player){
        player.setDisplayName(this.getColor() + "[" + this.getDisplayName() + "] " + player.getName() + ChatColor.RESET);
        player.setPlayerListName(player.getDisplayName());
        getScoreboardTeam.addPlayer(Bukkit.getOfflinePlayer(player.getName()));
        this.players.add(player);
    }

    public boolean deletePlayer(Player player){
        if (this.getPlayers().contains(player)){
            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getDisplayName());
            getScoreboardTeam.removePlayer(Bukkit.getOfflinePlayer(player.getName()));
            players.remove(player);
            if (plugin.getTaupeManager().getTaupes().contains(player)){
                plugin.getTaupeManager().removeTaupe(player);
            }
            return true;
        } else {
            return false;
        }
    }

    public void addOfflinePlayer(String player){
        offlinePlayers.add(player);
    }

    public boolean deleteOfflinePlayer(String player){
        return offlinePlayers.remove(player);
    }

    public ArrayList<String> getDisplayPlayersName(){
        ArrayList<String> list = new ArrayList<String>();
        for (Player player : this.getPlayers()){
            list.add(player.getDisplayName());
        }
        return list;
    }

    public void setTaupe(Taupe taupe){
        this.taupe = taupe;
    }

    public void reloadPlayersTags(){
        for (Player player : this.getPlayers()){
            player.setDisplayName(this.getColor() + "[" + this.getDisplayName() + "] " + player.getName() + ChatColor.RESET);
            player.setPlayerListName(player.getDisplayName());
        }
    }
}
