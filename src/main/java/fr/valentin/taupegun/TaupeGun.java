package fr.valentin.taupegun;

import fr.valentin.taupegun.commands.RevealCommand;
import fr.valentin.taupegun.commands.TaupeCommand;
import fr.valentin.taupegun.commands.TaupeGunCommand;
import fr.valentin.taupegun.commands.TeamCommand;
import fr.valentin.taupegun.config.TaupeGunConfig;
import fr.valentin.taupegun.episodes.EpisodeManager;
import fr.valentin.taupegun.files.FileManager;
import fr.valentin.taupegun.game.GameManager;
import fr.valentin.taupegun.listeners.GameEvents;
import fr.valentin.taupegun.listeners.PlayerEvents;
import fr.valentin.taupegun.listeners.WorldEvents;
import fr.valentin.taupegun.menus.SpectatorMenu;
import fr.valentin.taupegun.menus.TeamMenu;
import fr.valentin.taupegun.scoreboard.ScoreboardManager;
import fr.valentin.taupegun.taupe.TaupeManager;
import fr.valentin.taupegun.teams.TeamManager;
import fr.valentin.taupegun.timer.TimerManager;
import fr.valentin.taupegun.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Created by Valentin on 10/07/2015.
 */
public class TaupeGun extends JavaPlugin {

    public PluginManager pluginManager;
    private static TaupeGun instance;

    public static TaupeGun getInstance() {
        if (instance == null) {
            instance = new TaupeGun();
        }
        return instance;
    }
    private static Random random = new Random();

    @Override
    public void onEnable() {
        this.instance = this;
        this.pluginManager = this.getServer().getPluginManager();
        this.saveDefaultConfig();
        this.getFileManager().loadFiles();
        this.getLanguage = getPluginConfig().getLanguage;

        // Register events
        pluginManager.registerEvents(new PlayerEvents(), this);
        pluginManager.registerEvents(new WorldEvents(), this);
        pluginManager.registerEvents(new GameEvents(), this);
        pluginManager.registerEvents(new TeamMenu(), this);
        pluginManager.registerEvents(new SpectatorMenu(), this);

        // Register Commands
        getCommand("taupegun").setExecutor(new TaupeGunCommand());
        getCommand("taupe").setExecutor(new TaupeCommand());
        getCommand("reveal").setExecutor(new RevealCommand());
        getCommand("team").setExecutor(new TeamCommand());

        // Initialisation
        getWorldManager().initialise();
        getGameManager().initialiseGame();
        getTeamManager().loadTeams();
        getScoreboardManager().loadScoreboards();
        getEpisodeManager().initialise();

    }

    @Override
    public void onDisable() {
    }

    public static void log(String string){
        instance.getLogger().info(string);
    }

    /**
     *   @return The file manager.
     */
    public FileManager getFileManager(){
        return FileManager.getInstance();
    }

    /**
     * @return The plugin config
     */
    public TaupeGunConfig getPluginConfig(){
        return TaupeGunConfig.getInstance();
    }

    /**
     *   @return The episode manager.
     */
    public EpisodeManager getEpisodeManager(){
        return EpisodeManager.getInstance();
    }

    /**
     *   @return The episode manager.
     */
    public WorldManager getWorldManager(){
        return WorldManager.getInstance();
    }

    /**
     *   @return The game manager.
     */
    public GameManager getGameManager(){
        return GameManager.getInstance();
    }

    /**
     * @return The scoreboard manager.
     */
    public ScoreboardManager getScoreboardManager(){
        return ScoreboardManager.getInstance();
    }

    /**
     * @return The team manager.
     */
    public TeamManager getTeamManager(){
        return TeamManager.getInstance();
    }

    /**
     * @return The timer manager.
     */
    public TimerManager getTimerManager(){
        return TimerManager.getInstance();
    }

    /**
     * @return The taupe manager
     */
    public TaupeManager getTaupeManager(){
        return TaupeManager.getInstance();
    }

    /**
     * @return The teams file.
     */
    public FileConfiguration getTeamsFile(){
        return getFileManager().getFileConfiguration("teams.yml");
    }

    /**
     * @return The locations file.
     */
    public FileConfiguration getLocationsFile(){
        return getFileManager().getFileConfiguration("locations.yml");
    }

    /**
     * Translation system by zyuiop.
     * Get the correct translation for the key
     * @return the translation you can use.
     */
    public String getLanguage;
    private FileConfiguration translationFile;
    public String localize(String key) {
        if (translationFile == null){
            translationFile = getFileManager().getFileConfiguration("translations." + getLanguage + ".yml");
        }
        String tran = translationFile.getString(key);
        if (tran == null) {
            log("An error occured : impossible to find translations for " + key + " in translations." + getLanguage + ".yml file !");
            //tran = ChatColor.DARK_RED+"Fatal error : Failed to get translation for key "+ChatColor.AQUA+key;
            tran = "null";
        }
        else
            tran = ChatColor.translateAlternateColorCodes('&', tran); //Colors
        return tran;
    }
}
