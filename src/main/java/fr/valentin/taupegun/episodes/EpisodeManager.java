package fr.valentin.taupegun.episodes;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.Bukkit;

/**
 * Created by Valentin on 27/07/2015.
 */
public class EpisodeManager {

    private static EpisodeManager instance = new EpisodeManager();
    public static EpisodeManager getInstance(){return instance;}
    private EpisodeManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    private boolean episode = false;
    private int episodeNumber = 1;
    private int episodeLenght;

    public void initialise(){
        if (plugin.getPluginConfig().episodeIsEnable){
            episode = true;
            episodeLenght = plugin.getPluginConfig().episodeLenght;
        }
    }

    public void update(){
        if (plugin.getTimerManager().getMinutes() / episodeNumber == episodeLenght){
            broadcastEpisode();
            episodeNumber++;
        }
    }

    public void nextEpisode(){
        broadcastEpisode();
        setEpisode(getEpisode() + 1);
    }

    public void broadcastEpisode(){
        Bukkit.broadcastMessage(plugin.localize("end_message_episode")
                .replace("{prefix}", plugin.localize("prefix"))
                .replace("{episodes}", "" + episodeNumber));
    }

    public int getEpisode(){
        return episodeNumber;
    }

    public void setEpisode(int episodeNumber){
        this.episodeNumber = episodeNumber;
    }

    public long getEpisodeLenght(){
        return episodeLenght;
    }

    public boolean getEpisodeEnable(){
        return episode;
    }

}
