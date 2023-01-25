package de.headshotharp.web.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hibernate.Session;

import de.headshotharp.web.database.ChatEntry;
import de.headshotharp.web.database.ChatEntry.ChatType;
import de.headshotharp.web.database.Role;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.service.PermissionService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private DataExchangePlugin plugin;
    private DataProvider dp;
    private PermissionService permissionService;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        try (Session session = dp.openTransaction()) {
            Player player = event.getPlayer();
            User user = dp.user(session).findByPlayer(player).orElse(null);
            if (user != null) {
                player.sendMessage("Welcome back " + player.getName());
            } else {
                Role defaultRole = dp.role(session).findByLowestPower().orElse(null);
                user = dp.user(session).createUser(player, defaultRole).orElse(null);
                if (user != null) {
                    player.sendMessage("Welcome " + player.getName() + ", your DB entry has been created!");
                } else {
                    player.sendMessage("Error while creating a DB entry for you :(");
                }
            }
            if (user != null) {
                // log login message
                ChatEntry chatEntry = ChatEntry.builder().user(user).type(ChatType.LOGIN).build();
                dp.chat(session).persist(chatEntry);
            }
            if (user == null || user.getRole() == null) {
                resetPlayerData(player);
            } else {
                permissionService.setPermissions(player, session);
            }
            dp.commitTransaction(session);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        resetPlayerData(event.getPlayer());
        final String uuid = event.getPlayer().getUniqueId().toString();
        // log logout message
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Session session = dp.openTransaction()) {
                User user = dp.user(session).findByUuid(uuid).orElse(null);
                if (user != null) {
                    ChatEntry chatEntry = ChatEntry.builder().user(user).type(ChatType.LOGOUT).build();
                    dp.chat(session).persist(chatEntry);
                }
                dp.commitTransaction(session);
            }
        });
    }

    private void resetPlayerData(Player player) {
        permissionService.resetPermissions(player);
        permissionService.resetPlayerPrefix(player);
    }
}
