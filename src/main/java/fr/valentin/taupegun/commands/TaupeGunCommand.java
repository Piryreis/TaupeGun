package fr.valentin.taupegun.commands;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.episodes.EpisodeManager;
import fr.valentin.taupegun.game.GameManager;
import fr.valentin.taupegun.taupe.TaupeManager;
import fr.valentin.taupegun.teams.TeamManager;
import fr.valentin.taupegun.timer.TimerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Valentin on 05/08/2015.
 */
public class TaupeGunCommand implements CommandExecutor, TabCompleter{

    private TaupeGun plugin = TaupeGun.getInstance();
    private TeamManager teamManager = plugin.getTeamManager();
    private EpisodeManager episodeManager = plugin.getEpisodeManager();
    private TimerManager timerManager = plugin.getTimerManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && !((Player) commandSender).isOp()) {
            commandSender.sendMessage(plugin.localize("command_player_noop"));
            return true;
        }
        if (strings.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: " + command.getUsage());
            return true;
        }

        // -- /taupegun start --
        if (strings[0].equalsIgnoreCase("start")) {
            if (!GameManager.getInstance().getGameState(GameManager.gameStates.waiting)) {
                commandSender.sendMessage(plugin.localize("game_started").replace("{prefix}", plugin.localize("prefix")));
            } else {
                if (strings.length > 1){
                    if (strings[1].equalsIgnoreCase("force")) {
                        plugin.getGameManager().startGame();
                    }
                } else {

                    if (!teamManager.teamsIsFull()) {
                        commandSender.sendMessage(plugin.localize("teams_not_full").replace("{prefix}", plugin.localize("prefix")));

                        if (commandSender instanceof Player) {
                            TextComponent yesForce = new TextComponent(plugin.localize("force_yes"));
                            yesForce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/taupegun start force"));
                            TextComponent noForce = new TextComponent(plugin.localize("force_no"));

                            TextComponent forceMessage = new TextComponent(plugin.localize("force_start").replace("{prefix}", plugin.localize("prefix")));
                            forceMessage.addExtra(yesForce);
                            forceMessage.addExtra(" ");
                            forceMessage.addExtra(noForce);

                            Player player = (Player) commandSender;
                            player.spigot().sendMessage(forceMessage);

                        } else {
                            commandSender.sendMessage(plugin.localize("force_start_console").replace("{prefix}", plugin.localize("prefix")));
                        }
                    } else {
                        GameManager.getInstance().startGame();
                    }
                }
            }
        }

        // -- /taupegun setTime <x:h|x:m|x:s>
        if (strings[0].equalsIgnoreCase("settime")) {
            try {
                int value = Integer.parseInt(strings[1].split(":", 2)[0]);
                String timeSymbole = strings[1].split(":", 2)[1].trim();
                if (timeSymbole.equalsIgnoreCase("h")) {
                    timerManager.setHeures(value);
                }
                if (timeSymbole.equalsIgnoreCase("m")) {
                    timerManager.setMinutes(value);
                }
                if (timeSymbole.equalsIgnoreCase("s")) {
                    timerManager.setSecondes(value);
                }
                commandSender.sendMessage(plugin.localize("time_set")
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{time}", timerManager.getTime()));
            } catch (Exception e) {
                commandSender.sendMessage(ChatColor.RED + "Usage: /taupegun settime <x:h|x:m|x:s> \n Exemple: /taupegun settime 10:m");
            }
        }

        // -- /taupegun nextEpisode
        if (strings[0].equalsIgnoreCase("nextepisode")) {
            episodeManager.nextEpisode();
        }

        // -- /taupegun nextEpisode
        if (strings[0].equalsIgnoreCase("revealtaupes")) {
            TaupeManager.getInstance().generateTaupe();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        // -- /team --
        if (strings[0].equalsIgnoreCase("")){
            ArrayList<String> subCommand = new ArrayList<String>();
            subCommand.add("start");
            subCommand.add("settime");
            subCommand.add("nextepisode");
            subCommand.add("revealtaupes");
            Collections.sort(subCommand);
            return subCommand;
        }
        return null;
    }
}
