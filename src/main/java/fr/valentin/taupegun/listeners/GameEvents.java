package fr.valentin.taupegun.listeners;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.events.GameWonEvent;
import fr.valentin.taupegun.game.GameManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

/**
 * Created by Valentin on 08/08/2015.
 */
public class GameEvents implements Listener {

    private TaupeGun plugin = TaupeGun.getInstance();

    @EventHandler
    public void onGameWon(GameWonEvent event){
        String winnerName = event.getWinnerName();
        List<String> players = event.getPlayersNames();

        int message = 1;
        while (!plugin.localize("team_win_message_" + message).equals("null")){
            Bukkit.broadcastMessage(plugin.localize("team_win_message_" + message)
                    .replace("{team}", winnerName)
                    .replace("{players}", StringUtils.join(players, ", ")));
            message++;
        }

        plugin.getTimerManager().stopTimer();
        plugin.getGameManager().setGameState(GameManager.gameStates.ending);

    }

}
