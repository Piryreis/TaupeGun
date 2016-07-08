package fr.valentin.taupegun.scoreboard;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;

/**
 * Created by Valentin on 17/07/2015.
 */
public class Objective {

    private static Objective instance = new Objective();
    public static Objective getInstance(){return instance;}
    private Objective(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private ScoreboardManager scoreboardManager = plugin.getScoreboardManager().getInstance();

    private org.bukkit.scoreboard.Objective healthObjective = null;
    private org.bukkit.scoreboard.Objective scoreboardObjective = null;
    private org.bukkit.scoreboard.Objective killsObjective = null;

    public void initialise(){

        healthObjective = scoreboardManager.scoreboard.registerNewObjective("health", Criterias.HEALTH);
        healthObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        healthObjective.setDisplayName("health");

        killsObjective = scoreboardManager.scoreboard.registerNewObjective("kills", Criterias.PLAYER_KILLS);
        killsObjective.setDisplayName("kills");

        updateScoreboard();

        scoreboardManager.updateScoreboards();
    }

    public void updateScoreboard(){
        org.bukkit.scoreboard.Objective objective = null;
        try {
            objective = scoreboardManager.scoreboard.getObjective("TaupeGun");
            objective.setDisplaySlot(null);
            objective.unregister();
        } catch (Exception e) {
        }

        scoreboardObjective = scoreboardManager.scoreboard.registerNewObjective("TaupeGun", "dummy");
        scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboardObjective.setDisplayName(plugin.localize("scoreboard_name"));

        if (plugin.getGameManager().getGameState(GameManager.gameStates.waiting)){
            scoreboardObjective.getScore(Bukkit.getOfflinePlayer(plugin.localize("scoreboard_team")
                    .replace("{team}", "" + plugin.getTeamManager().getTeamsList().size()))).setScore(2);
            scoreboardObjective.getScore(Bukkit.getOfflinePlayer(plugin.localize("scoreboard_player")
                    .replace("{player}", "" + Bukkit.getOnlinePlayers().size()))).setScore(1);
        }
        if (plugin.getGameManager().getGameState(GameManager.gameStates.started)){
            if (plugin.getEpisodeManager().getEpisodeEnable()){
                scoreboardObjective.getScore(Bukkit.getOfflinePlayer(plugin.localize("scoreboard_episode")
                        .replace("{episodes}", "" + plugin.getEpisodeManager().getEpisode()))).setScore(4);
            }
            scoreboardObjective.getScore(Bukkit.getOfflinePlayer(plugin.localize("scoreboard_team")
                    .replace("{team}", "" + plugin.getTeamManager().getTeamsList().size()))).setScore(3);
            scoreboardObjective.getScore(Bukkit.getOfflinePlayer(plugin.localize("scoreboard_player")
                    .replace("{player}", "" + plugin.getTeamManager().playersInTeam()))).setScore(2);
            scoreboardObjective.getScore(Bukkit.getOfflinePlayer(plugin.localize("scoreboard_time")
                    .replace("{time}", "" + plugin.getTimerManager().getTime()))).setScore(1);
        }
    }

    public org.bukkit.scoreboard.Objective getKillsScoreobard(){
        return killsObjective;
    }
}
