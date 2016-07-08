package fr.valentin.taupegun.config;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Valentin on 01/09/2015.
 */
public class TaupeGunConfig {

    private static TaupeGunConfig instance = new TaupeGunConfig();
    public static TaupeGunConfig getInstance(){return instance;}
    private TaupeGunConfig(){}

    private FileConfiguration getConfiguration = TaupeGun.getInstance().getConfig();

    /**
     * Language
     */
    public String getLanguage = getConfiguration.getString("language", "fr");

    /**
     * Team
     */
    public int getTeamSize = getConfiguration.getInt("team.size", 1);
    public int getTeamNumber = getConfiguration.getInt("team.number", 0);

    /**
     * World
     */
    public String getWorldName = getConfiguration.getString("world.name", "world");
    public int getSpawnX = getConfiguration.getInt("world.spawn.x", 0);
    public int getSpawnY = getConfiguration.getInt("world.spawn.y", 120);
    public int getSpawnZ = getConfiguration.getInt("world.spawn.z", 0);
    public int getMapSize = getConfiguration.getInt("world.map_size", 2000);

    /**
     * Game
     */
    public boolean spectatorCanJoin = getConfiguration.getBoolean("game.join_spectator", true);
    public boolean canReconnect = getConfiguration.getBoolean("game.reconnection", true);
    public int whenTaupesAreUnveiled = getConfiguration.getInt("game.when_taupe_are_unveiled", 30);
    public boolean canDeathMessage = getConfiguration.getBoolean("game.death_message", false);
    public boolean canViewArchivements = getConfiguration.getBoolean("game.view_archivements", false);
    public boolean randomLocations = getConfiguration.getBoolean("game.random_locations", true);

    /**
     * Lobby
     */
    public boolean lobbyIsEnable = getConfiguration.getBoolean("lobby.enable", false);
    public String getLobbyName = getConfiguration.getString("lobby.name");
    public double getLobbyX = getConfiguration.getDouble("lobby.location.x");
    public double getLobbyY = getConfiguration.getDouble("lobby.location.y");
    public double getLobbyZ = getConfiguration.getDouble("lobby.location.z");

    /**
     * Episode
     */
    public boolean episodeIsEnable = getConfiguration.getBoolean("episodes.enable", true);
    public int episodeLenght = getConfiguration.getInt("episodes.lenght", 20);

    /**
     * Daylight cycle
     */
    public boolean doDayLightCycle = getConfiguration.getBoolean("dayLightCycle.do", false);
    public int defaultDayLightCycle = getConfiguration.getInt("dayLightCycle.time", 6000);

    /**
     * Weather
     */
    public boolean thereUsWeather = getConfiguration.getBoolean("weather", false);

}
