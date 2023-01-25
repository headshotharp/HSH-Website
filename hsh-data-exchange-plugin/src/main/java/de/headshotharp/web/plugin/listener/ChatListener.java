package de.headshotharp.web.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.service.PermissionService;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

@AllArgsConstructor
public class ChatListener implements Listener {

    private DataExchangePlugin plugin;
    private PermissionService permissionService;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncChatEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            String prefix = permissionService.getPlayerPrefix(player);
            String world = player.getLocation().getWorld().getName();
            Component message = Component.join(JoinConfiguration.noSeparators(),
                    text(NamedTextColor.WHITE, "[" + world + "]"),
                    text(NamedTextColor.DARK_GREEN, prefix),
                    text(NamedTextColor.WHITE, " "),
                    player.displayName(),
                    text(NamedTextColor.WHITE, ": "),
                    event.message());
            plugin.getServer().sendMessage(message);
            event.setCancelled(true);
        }
    }

    private TextComponent text(TextColor color, String s) {
        return Component.empty().color(color).content(s);
    }
}
