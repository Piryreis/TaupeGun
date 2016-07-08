package fr.valentin.taupegun.menus;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.teams.TGTeam;
import fr.valentin.taupegun.utils.Utils;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.PacketPlayOutOpenSignEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Valentin on 12/07/2015.
 */
public class TeamMenu implements Listener{

    private static TeamMenu instance = new TeamMenu();
    public static TeamMenu getInstance(){return instance;}

    private TaupeGun plugin = TaupeGun.getInstance();
    private Utils utils = Utils.getInstance();

    private HashMap<Integer, TGTeam> itemTeam = new HashMap<Integer, TGTeam>();

    public ItemStack getItem(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.localize("team_item_menu"));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event){
        Action a = event.getAction();
        ItemStack is = event.getItem();
        if (a == Action.PHYSICAL || is == null || is.getType() == Material.AIR)
            return;
        if (is.equals(this.getItem())){
            openMenu(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();

        try {
            if (inv.equals(inventory)) {
                event.setCancelled(true);
                p.closeInventory();

                if (item.getType().equals(Material.WOOL)){
                    TGTeam TGTeam = itemTeam.get(inv.first(item));

                    if (TGTeam.equals(plugin.getTeamManager().getPlayerTeam(p))){
                        TGTeam.deletePlayer(p);
                        p.sendMessage(plugin.localize("leave_team").replace("{team}",
                                TGTeam.getColor() + TGTeam.getDisplayName())
                                .replace("{prefix}", plugin.localize("prefix")));

                    } else {
                        if (TGTeam.getPlayers().size() < plugin.getConfig().getInt("team.size")){
                            if (plugin.getTeamManager().playerIsInTeam(p)){

                                TGTeam laterTeam = plugin.getTeamManager().getPlayerTeam(p);
                                laterTeam.deletePlayer(p);
                                p.sendMessage(plugin.localize("leave_team").replace("{team}",
                                        laterTeam.getColor() + laterTeam.getDisplayName())
                                        .replace("{prefix}", plugin.localize("prefix")));

                            }

                            TGTeam.addPlayer(p);
                            p.sendMessage(plugin.localize("join_team").replace("{team}",
                                    TGTeam.getColor() + TGTeam.getDisplayName())
                                    .replace("{prefix}", plugin.localize("prefix")));

                        } else {
                            p.sendMessage(plugin.localize("team_full").replace("{prefix}", plugin.localize("prefix")));
                        }
                    }
                } else if (item.getType().equals(Material.SIGN)){
                    //Rename team witch a signEditor;
                }
            }

        } catch (Exception e){
        }
    }

    private Inventory inventory = Bukkit.createInventory(null, 9, plugin.localize("team_menu_name"));
    public void openMenu(Player player){
        int size = Math.round(plugin.getTeamManager().getTeamsList().size()/9) * 9 + 36;
        int slot = 9;

        if (plugin.getTeamManager().playerIsInTeam(player)){
            inventory = Bukkit.createInventory(null, size + 9,  plugin.localize("team_menu_name"));
            ItemStack renameItem = new ItemStack(Material.SIGN, 1);
            ItemMeta renameItemMeta = renameItem.getItemMeta();
            renameItemMeta.setDisplayName(plugin.localize("rename_team_item"));
            renameItem.setItemMeta(renameItemMeta);
            inventory.setItem(size - 5, renameItem);

        } else {
            inventory = Bukkit.createInventory(null, size,  plugin.localize("team_menu_name"));
        }

        ArrayList<String> lore;
        for (TGTeam TGTeam : plugin.getTeamManager().getTeamsList()){
            ItemStack item = new ItemStack(Material.WOOL, 1, utils.getDyeColor(TGTeam.getColor()).getData());
            ItemMeta meta = item.getItemMeta();
            lore = new ArrayList<String>();
            for (Player p : TGTeam.getPlayers()){
                lore.add(TGTeam.getColor() + "- " + p.getName());
            }
            meta.setDisplayName(TGTeam.getColor()  + "" + ChatColor.BOLD + TGTeam.getDisplayName()
                    + " [" + lore.size() + "/" + plugin.getPluginConfig().getTeamSize + "]");
            slot++;
            switch (slot){
                case 9:
                case 18:
                    slot++;
                    break;
                case 17:
                case 26:
                    slot = slot + 2;
                    break;
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
            itemTeam.put(slot, TGTeam);
        }

        player.openInventory(inventory);
    }
}
