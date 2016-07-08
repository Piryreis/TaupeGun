package fr.valentin.taupegun.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Valentin on 08/08/2015.
 */
public class GameStartEvent extends Event {

    public GameStartEvent(){
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
