package fr.valentin.taupegun.game;

import fr.valentin.taupegun.events.GameStartEvent;
import fr.valentin.taupegun.events.GameWonEvent;
import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.menus.SpectatorMenu;
import fr.valentin.taupegun.teams.TGTeam;
import fr.valentin.taupegun.teams.TeamManager;
import fr.valentin.taupegun.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Valentin on 21/07/2015.
 */
public class GameManager {

    private static GameManager instance = new GameManager();
    public static GameManager getInstance(){return instance;}
    private GameManager(){}

    private TaupeGun plugin = TaupeGun.getInstance();
    private TeamManager teamManager = plugin.getTeamManager();

    private HashMap<TGTeam, Location> locations = new HashMap<TGTeam, Location>();
    private ArrayList<String> playersInGame = new ArrayList<String>();

    public static enum gameStates{
        waiting, started, ending,
    }

    private gameStates gameState;

    public gameStates getGameState(){
        return gameState;
    }

    public boolean getGameState(gameStates state){
        if (gameState.equals(state)) return true;
        else return false;
    }

    public void setGameState(gameStates state){
        gameState = state;
    }

    public void initialiseGame(){
        gameState = gameStates.waiting;
    }

    public void startGame(){
        try{
            World world = plugin.getServer().getWorld(plugin.getPluginConfig().getWorldName);
            world.setGameRuleValue("doDaylightCycle", ((Boolean) plugin.getPluginConfig().doDayLightCycle).toString());
            world.setTime(plugin.getPluginConfig().defaultDayLightCycle);
            world.setDifficulty(Difficulty.HARD);

            List<ConfigurationSection> locationSections = null;
            if (!plugin.getPluginConfig().randomLocations){
                locationSections = new ArrayList<ConfigurationSection>();
                for (String string : plugin.getLocationsFile().getKeys(false)) {
                    ConfigurationSection section = plugin.getLocationsFile().getConfigurationSection(string);
                    locationSections.add(section);
                }
            }

            for (TGTeam TGTeam : teamManager.getTeamsList()){
                if (TGTeam.getPlayers().isEmpty()){
                    System.out.println("delete team " + TGTeam.getName());
                    teamManager.deleteTeam(TGTeam);
                    //return;
                }
                if (plugin.getPluginConfig().randomLocations){
                    Location location = plugin.getWorldManager().randomMapLocation();
                    locations.put(TGTeam, location);
                } else {
                    try {
                        ConfigurationSection section = locationSections.get(0);
                        locations.put(TGTeam, new Location(world, section.getInt(".x"), 120, section.getInt(".z")));
                        locationSections.remove(0);
                    } catch (NullPointerException e){
                        Location location = plugin.getWorldManager().randomMapLocation();
                        locations.put(TGTeam, location);
                    }

                    /*for (String string : plugin.getLocationsFile().getKeys(false)){
                        ConfigurationSection section = plugin.getLocationsFile().getConfigurationSection(string);
                        locations.put(TGTeam, new Location(world, section.getInt(".x"), 120, section.getInt(".z")));
                    }*/
                }
            }
            List<String> startMessages = new ArrayList<String>();
            int message = 1;
            while (!plugin.localize("start_message_" + message).equals("null")){
                startMessages.add(plugin.localize("start_message_" + message)
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{name}", plugin.getDescription().getName())
                        .replace("{version}", plugin.getDescription().getVersion())
                        .replace("{author}", "Val'entin"));
                message++;
            }

            for (Player player : Bukkit.getOnlinePlayers()){
                if (teamManager.playerIsInTeam(player)){
                    TGTeam TGTeam = teamManager.getPlayerTeam(player);
                    Location location = locations.get(TGTeam);

                    playersInGame.add(player.getName());

                    player.setGameMode(GameMode.SURVIVAL);
                    player.setExp(0L + 0F);
                    player.setFlying(false);
                    player.setFoodLevel(20);
                    player.setHealth(20);
                    player.setExhaustion(5F);
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR),
                            new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
                    player.setLevel(0);
                    player.closeInventory();
                    player.getActivePotionEffects().clear();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 1000, true, true));

                    for(String string : startMessages){
                        player.sendMessage(string);
                    }

                    player.teleport(location);

                } else {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.getInventory().setItem(4, SpectatorMenu.getInstance().getItem());
                    player.sendMessage(plugin.localize("join_spectator").replace("{prefix}", plugin.localize("prefix")));
                }
            }

            plugin.getTimerManager().startTimer();
            plugin.getGameManager().setGameState(GameManager.gameStates.started);
            plugin.getScoreboardManager().updateScoreboards();

            plugin.pluginManager.callEvent(new GameStartEvent());

        } catch (Exception e){
            Bukkit.broadcastMessage(plugin.localize("start_error_message").replace("{prefix}", plugin.localize("prefix")));
            e.printStackTrace();
        }
    }

    public void testForWin(){
        if (teamManager.getTeamsList().size() == 1){
            TGTeam TGTeam = teamManager.getTeamsList().get(0);
            TaupeGun.log("Team win !");
            plugin.pluginManager.callEvent(new GameWonEvent(
                    TGTeam.getColor() + TGTeam.getDisplayName(),
                    TGTeam.getDisplayPlayersName()));
            return;
        }
        if (teamManager.getPlayersInTeam().equals(plugin.getTaupeManager().getPlayers())
                && plugin.getTaupeManager().taupeGenerated){
            TaupeGun.log("Taupe win !");
            plugin.pluginManager.callEvent(new GameWonEvent(
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "Taupes",
                    plugin.getTaupeManager().getDisplayTaupesName()));
            return;
        }


        /*
        if (teamManager.getPlayersInTeam().equals(plugin.getTaupeManager().getPlayers())){
            /*plugin.pluginManager.callEvent(new GameWonEvent(
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "Taupes",
                    plugin.getTaupeManager().getDisplayTaupesName()));*/
            //gameWonActions(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Taupes", plugin.getTaupeManager().getDisplayTaupesName());

        /*} else if (teamManager.getTeamsList().size() == 1){
            plugin.getLogger().info("super pouvoir");
            TGTeam TGTeam = teamManager.getTeamsList().get(0);

            /*Bukkit.getServer().getPluginManager().callEvent(new GameWonEvent(
                    TGTeam.getColor() + TGTeam.getDisplayName(),
                    TGTeam.getDisplayPlayersName()));*/
            //gameWonActions(TGTeam.getColor() + TGTeam.getDisplayName(), TGTeam.getDisplayPlayersName());

        //}*/
    }

    public ArrayList<String> getPlayersInGame(){
        return playersInGame;
    }

    public boolean removePlayerInGame(String player){
        return playersInGame.remove(player);
    }

    public void gameWonActions(String winnerName, List<String> playersName){
        String winner = winnerName;
        List<String> players = playersName;

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
