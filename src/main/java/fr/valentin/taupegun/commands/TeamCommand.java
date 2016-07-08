package fr.valentin.taupegun.commands;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.game.GameManager;
import fr.valentin.taupegun.teams.TGTeam;
import fr.valentin.taupegun.teams.TeamManager;
import fr.valentin.taupegun.utils.Utils;
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
 * Created by Valentin on 18/07/2015.
 */
public class TeamCommand implements CommandExecutor, TabCompleter {

    private TaupeGun plugin = TaupeGun.getInstance();
    private TeamManager teamManager = plugin.getTeamManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()){
            commandSender.sendMessage(plugin.localize("command_player_noop"));
            return true;
        } else {
            if (strings.length < 1) {
                commandSender.sendMessage(help());
            } else {

                // -- /team list --
                if (strings[0].equalsIgnoreCase("list")) {
                    for (TGTeam TGTeam : TeamManager.getInstance().getTeamsList()) {
                        commandSender.sendMessage(TGTeam.getColor() + "• Name: " + TGTeam.getName()
                                + " ; Display Name: " + TGTeam.getDisplayName()
                                + " ; Players: " + TGTeam.getDisplayPlayersName());
                    }
                    return true;
                }
                // -- /team join <player> <team>
                else if (strings[0].equals("join")) {
                    try {
                        String player = strings[1];
                        TGTeam TGTeam = teamManager.getTeam(strings[2]);
                        TGTeam.addPlayer(Bukkit.getPlayerExact(player));
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Usage: /team join <player> <team>");
                    }
                    return true;
                }

                // -- /team leave <player> <team>
                else if (strings[0].equals("leave")) {
                    try {
                        String player = strings[1];
                        TGTeam TGTeam = teamManager.getTeam(strings[2]);
                        TGTeam.deletePlayer(Bukkit.getPlayerExact(player));
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Usage: /team leave <player> <team>");
                    }
                    return true;
                }

                // -- /team add <name> <color>
                else if (strings[0].equals("add")) {
                    if (plugin.getGameManager().getGameState(GameManager.gameStates.waiting)) {
                        try {
                            String name = strings[1];
                            ChatColor color = ChatColor.valueOf(strings[2]);
                            teamManager.addTeam(name, name, color);
                            plugin.getScoreboardManager().updateScoreboards();
                        } catch (Exception e) {
                            commandSender.sendMessage(ChatColor.RED + "Usage: /team add <name> <color>");
                        }
                    } else {
                        commandSender.sendMessage(plugin.localize("game_started").replace("{prefix}", plugin.localize("prefix")));
                    }
                    return true;
                }

                // -- /team delete <team>
                else if (strings[0].equals("delete")) {
                    if (plugin.getGameManager().getGameState(GameManager.gameStates.waiting)) {
                        try {
                            TGTeam TGTeam = teamManager.getTeam(strings[1]);
                            teamManager.deleteTeam(TGTeam);
                            plugin.getScoreboardManager().updateScoreboards();
                        } catch (Exception e) {
                            commandSender.sendMessage(ChatColor.RED + "Usage: /team delete <team>");
                        }
                    } else {
                        commandSender.sendMessage(plugin.localize("game_started").replace("{prefix}", plugin.localize("prefix")));
                    }
                    return true;
                }

                // -- /team setname <team> <name...>
                else if (strings[0].equals("setname")) {
                    try {
                        TGTeam TGTeam = teamManager.getTeam(strings[1]);
                        StringBuilder name = new StringBuilder();
                        for (int result = 0; result < strings.length; result++){
                            if (result > 1){
                                if (result == 2){
                                    name.append(strings[result]);
                                } else {
                                    name.append(" ");
                                    name.append(strings[result]);
                                }
                            }
                        }
                        TGTeam.setDisplayName(name.toString());
                        commandSender.sendMessage(plugin.localize("team_rename_message")
                                .replace("{prefix}", plugin.localize("prefix"))
                                .replace("{team}", TGTeam.getColor() + TGTeam.getName())
                                .replace("{name}", TGTeam.getColor() + TGTeam.getDisplayName()));
                    } catch (Exception e) {
                        commandSender.sendMessage(ChatColor.RED + "Usage: /team setname <team> <name...>");
                    }
                    return true;
                }

                // -- Help --
                else {
                    commandSender.sendMessage(help());
                    return true;
                }
            }
        }
        return true;
    }

    public String help(){
        return
                "" + plugin.localize("team_command_title") + "\n"
                        + plugin.localize("team_command_list") + "\n"
                        + plugin.localize("team_command_join") + "\n"
                        + plugin.localize("team_command_leave") + "\n"
                        + plugin.localize("team_command_add") + "\n"
                        + plugin.localize("team_command_delete") + "\n"
                        + plugin.localize("team_command_setname") + "\n";
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        // -- /team --
        if (strings[0].equalsIgnoreCase("")){
            ArrayList<String> subCommand = new ArrayList<String>();
            subCommand.add("join");
            subCommand.add("leave");
            subCommand.add("add");
            subCommand.add("delete");
            subCommand.add("list");
            subCommand.add("setname");
            Collections.sort(subCommand);
            return subCommand;
        }
        // -- /team <join|leave> <player> <team> --
        if (strings[0].equalsIgnoreCase("join") || strings[0].equalsIgnoreCase("leave")){
            if (strings[1].equalsIgnoreCase("")){
                ArrayList<String> players = new ArrayList<String>();
                for (Player player : Bukkit.getOnlinePlayers()){
                    players.add(player.getName());
                }
                Collections.sort(players);
                return players;
            }
            if (strings[2].equalsIgnoreCase("")){
                ArrayList<String> teams = new ArrayList<String>();
                for (TGTeam TGTeam : teamManager.getTeamsList()){
                    teams.add(TGTeam.getName());
                }
                Collections.sort(teams);
                return teams;
            }
        }
        // -- /team add <name> <color>
        if (strings[0].equalsIgnoreCase("add")){
            if (strings[1].equalsIgnoreCase("")){
                ArrayList<String> names = new ArrayList<String>();
                names.add("name");
                Collections.sort(names);
                return names;
            }
            if (strings[2].equalsIgnoreCase("")){
                ArrayList<String> colors = new ArrayList<String>();
                for (ChatColor chatColor : ChatColor.values()){
                    if (chatColor == ChatColor.MAGIC || chatColor == ChatColor.UNDERLINE || chatColor == ChatColor.BOLD
                            || chatColor == ChatColor.RESET || chatColor == ChatColor.ITALIC || chatColor == ChatColor.STRIKETHROUGH){
                    } else {
                        colors.add(chatColor.name());
                    }
                }
                Collections.sort(colors);
                return colors;
            }
        }
        // -- /team delete <team>
        if (strings[0].equalsIgnoreCase("delete") || strings[0].equalsIgnoreCase("setname")){
            ArrayList<String> teams = new ArrayList<String>();
            for (TGTeam TGTeam : teamManager.getTeamsList()){
                teams.add(TGTeam.getName());
            }
            Collections.sort(teams);
            return teams;
        }
        return null;
    }

}
