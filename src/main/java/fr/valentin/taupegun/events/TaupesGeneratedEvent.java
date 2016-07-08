package fr.valentin.taupegun.events;

import fr.valentin.taupegun.taupe.Taupe;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

/**
 * Created by Valentin on 08/08/2015.
 */
public class TaupesGeneratedEvent extends Event {

    private ArrayList<Taupe> taupes;

    public TaupesGeneratedEvent(ArrayList<Taupe> taupes){
        this.taupes = taupes;
    }

    public ArrayList<Taupe> getTaupes(){
        return taupes;
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
