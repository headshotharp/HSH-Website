package de.headshotharp.web.plugin.listener;

import org.bukkit.event.Listener;

import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class PlayerJoinListener implements Listener {

    private DataProvider dp;
    private DataExchangePlugin plugin;

    public PlayerJoinListener(DataProvider dp, DataExchangePlugin plugin) {
        this.dp = dp;
        this.plugin = plugin;
    }
}
