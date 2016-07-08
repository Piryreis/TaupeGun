package fr.valentin.taupegun.listeners;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by Valentin on 07/08/2015.
 */
public class WorldEvents implements Listener {

    private TaupeGun plugin = TaupeGun.getInstance();

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent ev) {
        if (!plugin.getPluginConfig().thereUsWeather) {
            ev.setCancelled(true);
        }
    }

}
