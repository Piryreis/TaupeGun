package fr.valentin.taupegun.menus;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.scoreboard.Objective;
import fr.valentin.taupegun.teams.TGTeam;
import fr.valentin.taupegun.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Valentin on 21/07/2015.
 */
public class SpectatorMenu implements Listener{

    private static SpectatorMenu instance = new SpectatorMenu();
    public static SpectatorMenu getInstance(){return instance;}

    private TaupeGun plugin = TaupeGun.getInstance();

    private HashMap<Integer, TGTeam> itemTeam = new HashMap<Integer, TGTeam>();

    public ItemStack getItem(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.localize("spectator_item_menu"));
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
            openTeamInventory(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();

        try {
            if (item.equals(this.getItem())){
                openTeamInventory(player);
            }

            if (inv.equals(teamsInventory)) {
                event.setCancelled(true);
                player.closeInventory();
                if (item.getType().equals(Material.WOOL)) {
                    TGTeam TGTeam = itemTeam.get(inv.first(item));
                    openPlayersInventory(player, TGTeam);
                }
            }
            if (inv.equals(playersInventory)){
                event.setCancelled(true);
                player.closeInventory();
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                String skullPlayer = skullMeta.getOwner();
                player.teleport(Bukkit.getPlayerExact(skullPlayer).getLocation());
            }

        } catch (Exception e){

        }
    }

    private Inventory teamsInventory = Bukkit.createInventory(null, 9, plugin.localize("spectator_teams_menu_name"));
    public void openTeamInventory(Player player){
        int slot = 9;
        int size = Math.round(plugin.getTeamManager().getTeamsList().size() / 9) * 9 + 36;
        teamsInventory = Bukkit.createInventory(null, size, plugin.localize("spectator_teams_menu_name"));
        ArrayList<String> lore;
        for (TGTeam TGTeam : plugin.getTeamManager().getTeamsList()){
            ItemStack item = new ItemStack(Material.WOOL, 1, Utils.getInstance().getDyeColor(TGTeam.getColor()).getData());
            ItemMeta meta = item.getItemMeta();
            lore = new ArrayList<String>();
            for (Player p : TGTeam.getPlayers()){
                lore.add(TGTeam.getColor() + "- " + p.getName());
            }
            meta.setDisplayName(TGTeam.getColor()  + "" + ChatColor.BOLD + TGTeam.getDisplayName());
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
            teamsInventory.setItem(slot, item);
            itemTeam.put(slot, TGTeam);
        }
        player.openInventory(teamsInventory);
    }

    private Inventory playersInventory = Bukkit.createInventory(null, 9, plugin.localize("spectator_players_menu_name"));
    public void openPlayersInventory(Player player, TGTeam team){
        playersInventory.getTitle().replace("{team}", team.getColor() + team.getDisplayName());
        int size = Math.round(team.getPlayers().size() / 9) * 9 + 9;
        playersInventory = Bukkit.createInventory(null, size, plugin.localize("spectator_players_menu_name"));

        ArrayList<String> lore;
        for (Player p : team.getPlayers()){
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
            itemMeta.setOwner(p.getName());
            itemMeta.setDisplayName(team.getColor() + p.getDisplayName());
            lore = new ArrayList<String>();
            String health = p.getHealth() + " ❤";
            lore.add(ChatColor.RED + " - " + health);
            lore.add(ChatColor.GREEN + " - "
                    + Objective.getInstance().getKillsScoreobard().getScore(Bukkit.getOfflinePlayer(p.getName())).getScore()
                    + " kill");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            playersInventory.addItem(item);
        }
        player.openInventory(playersInventory);
    }
}
