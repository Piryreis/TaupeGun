package fr.valentin.taupegun.commands;

import fr.valentin.taupegun.TaupeGun;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Valentin on 29/07/2015.
 */
public class TaupeCommand implements CommandExecutor{

    private TaupeGun plugin = TaupeGun.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(plugin.localize("sender_is_not_player"));
        } else {
            Player player = (Player) commandSender;
            if (plugin.getTaupeManager().getPlayerTaupe(player) == null){
                player.sendMessage(plugin.localize("player_is_not_taupe").replace("{prefix}", plugin.localize("prefix")));
            } else {
                if (strings.length < 1){
                    player.sendMessage(ChatColor.RED + "Usage: " + command.getUsage());
                } else {
                    StringBuilder message = new StringBuilder();
                    for (int result = 0; result < strings.length; ++result) {
                        message.append(" ");
                        message.append(strings[result]);
                    }
                    plugin.getTaupeManager().taupeSendMessage(player, message.toString());
                }
            }
        }
        return true;
    }
}
