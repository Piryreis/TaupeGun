package fr.valentin.taupegun.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by Valentin on 17/07/2015.
 */
public class ScoreboardManager{

    private static ScoreboardManager instance = new ScoreboardManager();
    public static ScoreboardManager getInstance(){return instance;}
    private ScoreboardManager(){}

    private org.bukkit.scoreboard.ScoreboardManager scoreboardManager;

    public Scoreboard scoreboard = this.getNewScoreboard();

    public org.bukkit.scoreboard.ScoreboardManager getManager(){
        scoreboardManager = Bukkit.getScoreboardManager();
        return scoreboardManager;
    }

    public void loadScoreboards(){
        Objective.getInstance().initialise();
        Team.getInstance().initialise();
    }

    public void updateScoreboards(){
        getObjective().updateScoreboard();
        for (Player player : Bukkit.getOnlinePlayers()){
            player.setScoreboard(scoreboard);
        }
    }

    /**
     * @return The objective class.
     */
    public Objective getObjective(){
        return Objective.getInstance();
    }

    /**
     * @return The team class.
     */
    public Team getTeam(){
        return Team.getInstance();
    }

    public org.bukkit.scoreboard.Scoreboard getMainScoreboard() {
        return this.getManager().getMainScoreboard();
    }

    public org.bukkit.scoreboard.Scoreboard getNewScoreboard() {
        return this.getManager().getNewScoreboard();
    }

}
