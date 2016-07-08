package fr.valentin.taupegun.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Created by Valentin on 08/08/2015.
 */
public class GameWonEvent extends Event {

    private String winnerName;
    private List<String> playersNames;

    public GameWonEvent(String winnerName, List<String> playersNames){
        this.winnerName = winnerName;
        this.playersNames = playersNames;

    }

    public String getWinnerName(){
        return winnerName;
    }

    public void setWinnerName(String winnerName){
        this.winnerName = winnerName;
    }

    public List<String> getPlayersNames(){
        return playersNames;
    }

    public void setPlayersName(String playersName){
        playersNames.add(playersName);
    }

    public boolean removePlayerName(String playerName){
        if (playersNames.contains(playerName)){
            playersNames.remove(playerName);
            return true;
        } else {
            return false;
        }
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
