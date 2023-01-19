package de.headshotharp.web.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.headshotharp.web.plugin.hibernate.DataProvider;

public class PlayerInteractListener implements Listener {

    private DataProvider dp;

    public PlayerInteractListener(DataProvider dp) {
        this.dp = dp;
    }

    // TODO: cache and flush if changed

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            dp.user().incrementBrokenBlocks(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            dp.user().incrementPlacedBlocks(event.getPlayer());
        }
    }
}
