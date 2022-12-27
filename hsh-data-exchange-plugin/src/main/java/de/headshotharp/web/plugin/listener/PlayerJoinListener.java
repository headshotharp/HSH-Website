package de.headshotharp.web.plugin.listener;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class PlayerJoinListener implements Listener {

    private DataProvider dp;

    public PlayerJoinListener(DataProvider dp) {
        this.dp = dp;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Optional<User> user = dp.user().findByPlayer(event.getPlayer());
        if (user.isPresent()) {
            event.getPlayer().sendMessage("Welcome back " + event.getPlayer().getName());
        } else {
            dp.user().createUser(event.getPlayer());
            event.getPlayer()
                    .sendMessage("Welcome " + event.getPlayer().getName() + ", your DB entry has been created!");
        }
    }
}
