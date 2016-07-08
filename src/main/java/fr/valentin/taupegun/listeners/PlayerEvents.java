package fr.valentin.taupegun.listeners;

import fr.valentin.taupegun.TaupeGun;
import fr.valentin.taupegun.game.GameManager;
import fr.valentin.taupegun.menus.SpectatorMenu;
import fr.valentin.taupegun.menus.TeamMenu;
import fr.valentin.taupegun.scoreboard.ScoreboardManager;
import fr.valentin.taupegun.teams.TGTeam;
import fr.valentin.taupegun.teams.TeamManager;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


/**
 * Created by Valentin on 10/07/2015.
 */
public class PlayerEvents implements Listener {

    private TaupeGun plugin = TaupeGun.getInstance();
    private GameManager gameManager = plugin.getGameManager();
    private TeamManager teamManager = plugin.getTeamManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        String player = p.getName();

        int maxPlayers = plugin.getPluginConfig().getTeamNumber * plugin.getPluginConfig().getTeamSize;

        if (gameManager.getGameState(GameManager.gameStates.waiting)){

            event.setJoinMessage(plugin.localize("player_join_server")
                    .replace("{prefix}", plugin.localize("prefix"))
                    .replace("{player}", player)
                    .replace("{nmbPlayer}", "" + Bukkit.getOnlinePlayers().size())
                    .replace("{maxPlayers}", "" + maxPlayers));

            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setItem(4, TeamMenu.getInstance().getItem());

            World world = plugin.getWorldManager().world;
            double x = plugin.getPluginConfig().getSpawnX;
            double y = plugin.getPluginConfig().getSpawnY;
            double z = plugin.getPluginConfig().getSpawnZ;
            p.teleport(new Location(world, x, y, z));

            if (teamManager.getOfflinePlayerTeam(player) != null){
                TGTeam TGTeam = teamManager.getOfflinePlayerTeam(player);
                TGTeam.deleteOfflinePlayer(player);
                TGTeam.addPlayer(p);
                p.sendMessage(plugin.localize("join_team").replace("{team}",
                        TGTeam.getColor() + TGTeam.getDisplayName())
                        .replace("{prefix}", plugin.localize("prefix")));
            }
        }

        if (gameManager.getGameState(GameManager.gameStates.started)) {
            if (gameManager.getPlayersInGame().contains(player)) {
                event.setJoinMessage(plugin.localize("player_reconnected")
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{player}", player));
            } else {
                event.setJoinMessage(plugin.localize("spectator_join_server")
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{player}", player));
                p.setGameMode(GameMode.SPECTATOR);
                p.getInventory().setItem(4, SpectatorMenu.getInstance().getItem());
            }
        }

        IChatBaseComponent textTitle = IChatBaseComponent.ChatSerializer.a(ChatColor.GOLD  + "Taupe" + ChatColor.RED + "Gun");
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, textTitle, 20, 60, 20);

        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(title);


        ScoreboardManager.getInstance().updateScoreboards();
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event){
        Player p = event.getPlayer();
        String player = p.getName();
        if (gameManager.getGameState(GameManager.gameStates.started) && gameManager.getPlayersInGame().contains(player)){
            if (plugin.getPluginConfig().canReconnect) {
                event.setQuitMessage(plugin.localize("player_in_game_left_server")
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{player}", p.getDisplayName()));
            } else {
                event.setQuitMessage(plugin.localize("player_left_server")
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{player}", p.getDisplayName()));
                teamManager.getPlayerTeam(p).deletePlayer(p);
                gameManager.removePlayerInGame(p.getName());
                for (Player p1 : Bukkit.getOnlinePlayers()) {
                    p1.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
                }
            }
            TGTeam TGTeam = teamManager.getPlayerTeam(p);
            if (TGTeam.getPlayers().size() == 1) {
                teamManager.deleteTeam(TGTeam);
                Bukkit.broadcastMessage(plugin.localize("eliminated_team")
                        .replace("{prefix}", plugin.localize("prefix"))
                        .replace("{team}", TGTeam.getColor() + TGTeam.getDisplayName()));
            }
            if (plugin.getTaupeManager().getPlayerTaupe(p) != null){
                plugin.getTaupeManager().removeTaupe(p);
            }
        } else {
            if (teamManager.playerIsInTeam(p)){
                teamManager.getPlayerTeam(p).deletePlayer(p);
            }
            event.setQuitMessage(plugin.localize("player_left_server")
                    .replace("{prefix}", plugin.localize("prefix"))
                    .replace("{player}", p.getDisplayName()));
        }
        ScoreboardManager.getInstance().updateScoreboards();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player p = event.getEntity();
        if (gameManager.getGameState(GameManager.gameStates.started)) {
            if (teamManager.playerIsInTeam(p)) {
                TGTeam TGTeam = teamManager.getPlayerTeam(p);
                if (!plugin.getPluginConfig().canDeathMessage) {
                    event.setDeathMessage(plugin.localize("death_message")
                            .replace("{prefix}", plugin.localize("prefix"))
                            .replace("{player}", p.getDisplayName()));
                    TGTeam.deletePlayer(p);
                    gameManager.removePlayerInGame(p.getName());
                    if (TGTeam.getPlayers().size() == 0) {
                        teamManager.deleteTeam(TGTeam);
                        Bukkit.broadcastMessage(plugin.localize("eliminated_team")
                                .replace("{prefix}", plugin.localize("prefix"))
                                .replace("{team}", TGTeam.getColor() + TGTeam.getDisplayName()));
                    }
                    if (plugin.getTaupeManager().getPlayerTaupe(p) != null){
                        plugin.getTaupeManager().removeTaupe(p);
                    }
                } else {
                    event.setDeathMessage(plugin.localize("prefix") + event.getDeathMessage());
                }

                try {
                    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                    skullMeta.setOwner(p.getName());
                    skullMeta.setDisplayName(p.getDisplayName());
                    skull.setItemMeta(skullMeta);
                    p.getWorld().dropItem(p.getLocation(), skull);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Player p1 : Bukkit.getOnlinePlayers()) {
                    p1.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (gameManager.getGameState(GameManager.gameStates.started)) {
            Player player = event.getPlayer();
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().setItem(4, SpectatorMenu.getInstance().getItem());
            player.sendMessage(plugin.localize("join_spectator").replace("{prefix}", plugin.localize("prefix")));
        }
    }

    @EventHandler
    public void onPlayerArchivements(PlayerAchievementAwardedEvent event){
        if (!gameManager.getGameState(GameManager.gameStates.started)){
            event.setCancelled(true);
        } else {
            if (!plugin.getPluginConfig().canViewArchivements){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPreLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();
        if (gameManager.getGameState(GameManager.gameStates.started)) {
            if (!(plugin.getPluginConfig().canReconnect && gameManager.getPlayersInGame().contains(player.getName())
                    || plugin.getPluginConfig().spectatorCanJoin)) {
                event.disallow(null, plugin.localize("game_started").replace("{prefix}", plugin.localize("prefix")));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String player = p.getName();
        String message = event.getMessage();
        if (gameManager.getGameState(GameManager.gameStates.started)){
            if (teamManager.playerIsInTeam(p)){
                if (!message.substring(0, 1).equalsIgnoreCase("!")){
                    event.setCancelled(true);
                    for (Player teamPlayer : teamManager.getPlayerTeam(p).getPlayers()){
                        teamPlayer.sendMessage(plugin.localize("team_chat_prefix").replace("{player}", p.getDisplayName())
                                + event.getMessage());
                    }
                }
                else {
                    event.setMessage(event.getMessage().replaceFirst("!", ""));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if (GameManager.getInstance().getGameState(GameManager.gameStates.waiting)){
            if (!event.getPlayer().isOp()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInterract(PlayerInteractEvent event){
        if (GameManager.getInstance().getGameState(GameManager.gameStates.waiting)) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDomage(EntityDamageEvent event){
        if (gameManager.getGameState(GameManager.gameStates.waiting)){
            if (event.getEntity() instanceof Player){
                event.setCancelled(true);
            }
        }
    }

}
