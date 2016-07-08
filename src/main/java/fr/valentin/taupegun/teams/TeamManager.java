package fr.valentin.taupegun.teams;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Valentin on 10/07/2015.
 */
public class TeamManager {

    private static TeamManager instance = new TeamManager();
    public static TeamManager getInstance(){return instance;}
    private TeamManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private HashMap<String, TGTeam> teams = new HashMap<String, TGTeam>();

    public void loadTeams(){
        int team = plugin.getPluginConfig().getTeamNumber;
        int teamRegisted = 0;
        for(String str : plugin.getTeamsFile().getKeys(false)){
            if (teamRegisted < team) {
                ConfigurationSection section = plugin.getTeamsFile().getConfigurationSection(str);

                String name = section.getString(".name");
                String displayName = section.getString(".display_name");
                ChatColor color = ChatColor.valueOf(section.getString(".color"));

                this.addTeam(name, displayName, color);

                TGTeam TGTeam = getTeam(name);
                for (String string : section.getStringList(".players")) {
                    TGTeam.addOfflinePlayer(string);
                }
                if (section.getString(".taupe") != null) {
                    Player taupe = Bukkit.getPlayerExact(section.getString(".taupe"));
                    plugin.getTaupeManager().addTaupe(TGTeam, taupe);
                }
                plugin.log("New team was loaded: " + "Name: " + name);
            }
            teamRegisted++;
        }
    }

    public boolean addTeam(String name, String displayName, ChatColor color){
        if (    color.equals(ChatColor.ITALIC) ||
                color.equals(ChatColor.MAGIC) ||
                color.equals(ChatColor.RESET) ||
                color.equals(ChatColor.BOLD) ||
                color.equals(ChatColor.UNDERLINE) ||
                color.equals(ChatColor.STRIKETHROUGH)){
            color = ChatColor.WHITE;
        }
        return registerNewTeam(new TGTeam(name, displayName, color));
    }

    public boolean registerNewTeam(TGTeam TGTeam){
        if (teams.containsValue(TGTeam)) return false;
        else teams.put(TGTeam.getName(), TGTeam); return true;
    }

    public boolean deleteTeam(String teamName){
        return teams.keySet().remove(teamName);
    }

    public boolean deleteTeam(TGTeam TGTeam){
        return teams.values().remove(TGTeam);
    }

    public boolean teamExiste(String name){
        return teams.keySet().contains(name);
    }

    public boolean teamExiste(TGTeam TGTeam){
        return teams.values().contains(TGTeam);
    }

    public ArrayList<TGTeam> getTeamsList(){
        ArrayList<TGTeam> teamList = new ArrayList<TGTeam>();
        teamList.addAll(teams.values());
        return teamList;
    }

    public ArrayList<String> getTeamsName(){
        ArrayList<String> nameList = new ArrayList<String>();
        nameList.addAll(teams.keySet());
        return nameList;
    }

    public HashMap<String, TGTeam> getTeamsMap(){
        return teams;
    }

    public TGTeam getPlayerTeam(Player player){
        TGTeam tr = null;
        for (TGTeam TGTeam : this.getTeamsList()){
            if (TGTeam.getPlayers().contains(player)) {
                tr = TGTeam;
            }
        }
        return tr;
    }

    public TGTeam getOfflinePlayerTeam(String player){
        TGTeam tr = null;
        for (TGTeam TGTeam : this.getTeamsList()){
            if (TGTeam.getOfflinePlayers().contains(player)){
                tr = TGTeam;
            }
        }
        return tr;
    }

    public TGTeam getTeam(String name){
        return teams.get(name);
    }

    public boolean playerIsInTeam(Player player){
        if (this.getPlayerTeam(player) == null) return false;
        else return true;
    }

    public boolean teamIsFull(TGTeam TGTeam){
        if (TGTeam.getPlayers().size() == plugin.getPluginConfig().getTeamSize) return true;
        else return false;
    }

    public boolean teamsIsFull(){
        boolean teamsfull = true;
        for (TGTeam TGTeam : this.getTeamsList()){
            if (!this.teamIsFull(TGTeam)){
                teamsfull = false;
            }
        }
        return teamsfull;
    }

    public int playersInTeam(){
        int totalPlayers = 0;
        for (TGTeam TGTeam : this.getTeamsList()){
            totalPlayers = totalPlayers + TGTeam.getPlayers().size();
        }
        return totalPlayers;
    }

    public List<Player> getPlayersInTeam(){
        List<Player> players = new ArrayList<Player>();
        for (TGTeam TGTeam : this.getTeamsList()){
            players.addAll(TGTeam.getPlayers());
        }
        return players;
    }
}