package de.headshotharp.web.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hibernate.Session;

import de.headshotharp.web.database.Role;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.permissions.PermissionService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

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
                User createdUser = dp.user(session).createUser(player, defaultRole).orElse(null);
                if (createdUser != null) {
                    player.sendMessage("Welcome " + player.getName() + ", your DB entry has been created!");
                    user = createdUser;
                } else {
                    player.sendMessage("Error while creating a DB entry for you :(");
                }
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
    }

    private void resetPlayerData(Player player) {
        permissionService.resetPermissions(player);
        permissionService.resetPlayerPrefix(player);
    }
}
