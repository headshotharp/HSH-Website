package de.headshotharp.web.plugin;

import de.headshotharp.plugin.base.LoggablePlugin;
import de.headshotharp.plugin.base.command.CommandRegistry;
import de.headshotharp.plugin.base.config.ConfigService;
import de.headshotharp.web.plugin.config.Config;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.listener.PlayerJoinListener;

public class DataExchangePlugin extends LoggablePlugin {

    private boolean enableSuccessful = false;

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
            dp = new DataProvider(config.getDatabase(), DataExchangePlugin.class);
            info("Connected to database");
        } catch (Exception e) {
            error("Error while connecting to database", e);
            return;
        }
        try {
            PlayerJoinListener playerJoinListener = new PlayerJoinListener(dp, this);
            getServer().getPluginManager().registerEvents(playerJoinListener, this);
        } catch (Exception e) {
            error("Error while registering listeners", e);
            return;
        }
        try {
            CommandRegistry<DataExchangePlugin> commandRegistry = new CommandRegistry<>(this, DataExchangePlugin.class);
            getCommand("dataexchange").setExecutor(commandRegistry);
            getCommand("dataexchange").setTabCompleter(commandRegistry);
        } catch (Exception e) {
            error("Error while registering commands", e);
            return;
        }
        enableSuccessful = true;
    }

    public boolean isEnableSuccessful() {
        return enableSuccessful;
    }
}
