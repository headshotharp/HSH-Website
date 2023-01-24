package de.headshotharp.web.plugin;

import java.io.IOException;

import de.headshotharp.plugin.base.LoggablePlugin;
import de.headshotharp.plugin.base.command.CommandRegistry;
import de.headshotharp.plugin.base.config.ConfigService;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.command.BlocksCommand;
import de.headshotharp.web.plugin.command.permissions.PromoteCommand;
import de.headshotharp.web.plugin.config.Config;
import de.headshotharp.web.plugin.dataimport.DataImportService;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.listener.ChatListener;
import de.headshotharp.web.plugin.listener.PlayerInteractListener;
import de.headshotharp.web.plugin.listener.PlayerJoinListener;
import de.headshotharp.web.plugin.permissions.PermissionService;

public class DataExchangePlugin extends LoggablePlugin {

    @Override
    public void onEnable() {
        ConfigService<Config> configService = new ConfigService<>(Config.class, getName());
        try {
            if (!configService.getConfigFile().exists()) {
                configService.saveConfig(Config.getDefaultConfig());
            }
        } catch (IOException e) {
            warn("Could not save default config", e);
        }
        Config config;
        try {
            config = configService.readConfig();
        } catch (Exception e) {
            throw new IllegalStateException("Error while loading config", e);
        }
        DataProvider dp;
        try {
            dp = new DataProvider(config.getDatabase(), User.class);
            info("Connected to database");
        } catch (Exception e) {
            throw new IllegalStateException("Error while connecting to database", e);
        }
        PermissionService permissionService = new PermissionService(this, dp);
        try {
            PlayerJoinListener playerJoinListener = new PlayerJoinListener(dp, permissionService);
            getServer().getPluginManager().registerEvents(playerJoinListener, this);
            PlayerInteractListener playerInteractListener = new PlayerInteractListener(dp);
            getServer().getPluginManager().registerEvents(playerInteractListener, this);
            ChatListener chatListener = new ChatListener(this, permissionService);
            getServer().getPluginManager().registerEvents(chatListener, this);
        } catch (Exception e) {
            throw new IllegalStateException("Error while registering listeners", e);
        }
        try {
            // permissions
            new CommandRegistry<>("pm", this, DataExchangePlugin.class, PromoteCommand.class, true, dp,
                    permissionService).registerCommands();
            // blocks
            new BlocksCommand(this, dp).registerCommands();
        } catch (Exception e) {
            throw new IllegalStateException("Error while registering commands", e);
        }
        // import data
        new DataImportService(getName(), dp).importData();
    }
}
