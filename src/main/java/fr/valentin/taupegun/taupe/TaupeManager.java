package fr.valentin.taupegun.taupe;

import fr.valentin.taupegun.events.TaupesGeneratedEvent;
import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.teams.TGTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Valentin on 28/07/2015.
 */
public class TaupeManager {

    private static TaupeManager instance = new TaupeManager();
    public static TaupeManager getInstance(){return instance;}
    private TaupeManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();

    public boolean taupeGenerated = false;
    private ArrayList<Taupe> taupes = new ArrayList<Taupe>();
    private static Random random = new Random();

    public void testForTaupe(){
        if (!taupeGenerated){
            if (plugin.getTimerManager().getMinutes() == plugin.getPluginConfig().whenTaupesAreUnveiled){
                generateTaupe();
            }
        }
    }

    public void generateTaupe(){
        Bukkit.broadcastMessage(plugin.localize("generate_taupe").replace("{prefix}", plugin.localize("prefix")));
        List<String> taupeMessages = new ArrayList<String>();
        int message = 1;
        while (!plugin.localize("taupe_message_" + message).equals("null")){
            taupeMessages.add(plugin.localize("taupe_message_" + message).replace("{prefix}", plugin.localize("prefix")));
            message++;
        }
        for (TGTeam TGTeam : plugin.getTeamManager().getTeamsList()) {
            if (TGTeam.getTaupe() != null && !TGTeam.getTaupe().getPlayer().isOnline()) {
                plugin.log(plugin.localize("taupe_not_found")
                        .replace("{taupe}", TGTeam.getTaupe().getName())
                        .replace("{team}", TGTeam.getName()));
                generateRandomTaupe(TGTeam);
            } else {
                generateRandomTaupe(TGTeam);
            }
            Player taupe = TGTeam.getTaupe().getPlayer();
            for (String string : taupeMessages){
                taupe.sendMessage(string);
            }
        }
        plugin.pluginManager.callEvent(new TaupesGeneratedEvent(taupes));
        taupeGenerated = true;
    }

    public void generateRandomTaupe(TGTeam TGTeam){
        if (TGTeam.getPlayers().size() != 0) {
            int taupeRandom = random.nextInt(TGTeam.getPlayers().size());
            Player taupe = TGTeam.getPlayers().get(taupeRandom);
            addTaupe(TGTeam, taupe);
        }
    }

    public void addTaupe(TGTeam TGTeam, Player player){
        int id = taupes.size() + 1;
        Taupe taupe = new Taupe(player, id);
        taupes.add(taupe);
        TGTeam.setTaupe(taupe);
    }

    public ArrayList<Taupe> getTaupes(){
        return taupes;
    }

    public boolean removeTaupe(Taupe taupe){
        return taupes.remove(taupe);
    }

    public boolean removeTaupe(Player player){
        Taupe taupe = this.getPlayerTaupe(player);
        return this.removeTaupe(taupe);
    }

    public Taupe getPlayerTaupe(Player player){
        Taupe taupe = null;
        for (Taupe taupes : getTaupes()){
            if (taupes.getPlayer().equals(player)){
                taupe = taupes;
            }
        }
        return taupe;
    }

    public ArrayList<String> getDisplayTaupesName(){
        ArrayList<String> taupesName = new ArrayList<String>();
        for (Taupe taupe : this.getTaupes()){
            taupesName.add(taupe.getPlayer().getDisplayName());
        }
        return taupesName;
    }

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<Player>();
        for (Taupe taupe : this.getTaupes()){
            players.add(taupe.getPlayer());
        }
        return players;
    }

    public void taupeSendMessage(Player taupe, String message){
        String string = plugin.localize("taupe_message")
                .replace("{number}", "" + getPlayerTaupe(taupe).getId())
                .replace("{message}", message);

        for (Taupe taupes1 : this.taupes){
            taupes1.getPlayer().sendMessage(string);
        }
    }

    public void revealTaupe(Player player){
        if (!getPlayerTaupe(player).isRevelated){
            Taupe taupe = getPlayerTaupe(player);
            Bukkit.broadcastMessage(plugin.localize("reveal_message")
                    .replace("{prefix}", plugin.localize("prefix"))
                    .replace("{player}", player.getDisplayName()).replace("{id}", "" + taupe.getId()));
            player.setDisplayName(plugin.localize("taupe_prefix").replace("{id}", "" + taupe.getId()) + player.getDisplayName());
            player.setPlayerListName(player.getDisplayName());
            taupe.isRevelated = true;
        }
    }
}