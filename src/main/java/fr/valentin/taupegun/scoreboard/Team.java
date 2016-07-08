package fr.valentin.taupegun.scoreboard;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.teams.TGTeam;
import org.bukkit.Bukkit;

/**
 * Created by Valentin on 10/07/2015.
 */
public class Team {

    private static Team instance = new Team();
    public static Team getInstance(){return instance;}
    private Team(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private ScoreboardManager scoreboardManager = plugin.getScoreboardManager();

    public void initialise(){
        for (TGTeam TGTeam : plugin.getTeamManager().getTeamsList()){
            TGTeam.getScoreboardTeam = scoreboardManager.scoreboard.registerNewTeam(TGTeam.getName());
            this.setDisplayName(TGTeam);
            TGTeam.getScoreboardTeam.setCanSeeFriendlyInvisibles(true);

        }
        scoreboardManager.updateScoreboards();
    }

    public boolean setDisplayName(TGTeam TGTeam){
        if (TGTeam.getDisplayName().length() > 11){
            Bukkit.getLogger().warning("The name of the team " + TGTeam.getDisplayName()
                    + " is too long, it must be less than 11 characters if you want the scoreboard supports it.");
            TGTeam.getScoreboardTeam.setDisplayName(TGTeam.getDisplayName());
            TGTeam.getScoreboardTeam.setPrefix(TGTeam.getColor() + "• ");
            return false;
        } else {
            TGTeam.getScoreboardTeam.setDisplayName(TGTeam.getDisplayName());
            TGTeam.getScoreboardTeam.setPrefix(TGTeam.getColor() + "[" + TGTeam.getDisplayName() + "] ");
            return true;
        }
    }

}