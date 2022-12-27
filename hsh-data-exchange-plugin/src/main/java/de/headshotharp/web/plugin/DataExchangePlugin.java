package de.headshotharp.web.plugin;

import de.headshotharp.plugin.base.LoggablePlugin;
import de.headshotharp.plugin.base.config.ConfigService;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.command.BlocksCommand;
import de.headshotharp.web.plugin.config.Config;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.listener.PlayerInteractListener;
import de.headshotharp.web.plugin.listener.PlayerJoinListener;

public class DataExchangePlugin extends LoggablePlugin {

    private boolean enabledSuccessful = false;

    @Override
    public void onEnable() {
        ConfigService<Config> configService = new ConfigService<>(Config.class, getName());
        try {
            if (!configService.getConfigFile().exists()) {
                configService.saveConfig(Config.getDefaultConfig());
            }
        } catch (Exception e) {
            error("Error while saving default config", e);
            return;
        }
        Config config;
        try {
            config = configService.readConfig();
        } catch (Exception e) {
            error("Error while loading config", e);
            return;
        }
        DataProvider dp;
        try {
            dp = new DataProvider(config.getDatabase(), User.class);
            info("Connected to database");
        } catch (Exception e) {
            error("Error while connecting to database", e);
            return;
        }
        try {
            PlayerJoinListener playerJoinListener = new PlayerJoinListener(dp);
            getServer().getPluginManager().registerEvents(playerJoinListener, this);
            PlayerInteractListener playerInteractListener = new PlayerInteractListener(dp);
            getServer().getPluginManager().registerEvents(playerInteractListener, this);
        } catch (Exception e) {
            error("Error while registering listeners", e);
            return;
        }
        try {
            /*-
            CommandRegistry<DataExchangePlugin> commandRegistry = new CommandRegistry<>(this, DataExchangePlugin.class);
            getCommand("data").setExecutor(commandRegistry);
            getCommand("data").setTabCompleter(commandRegistry);
            */
            BlocksCommand blocksCommand = new BlocksCommand(dp);
            getCommand("blocks").setExecutor(blocksCommand);
            getCommand("blocks").setTabCompleter(blocksCommand);
        } catch (Exception e) {
            error("Error while registering commands", e);
            return;
        }
        enabledSuccessful = true;
    }

    public boolean isEnabledSuccessful() {
        return enabledSuccessful;
    }
}
