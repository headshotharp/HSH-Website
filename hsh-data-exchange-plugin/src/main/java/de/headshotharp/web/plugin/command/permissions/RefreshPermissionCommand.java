package de.headshotharp.web.plugin.command.permissions;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.permissions.PermissionService;

public class RefreshPermissionCommand extends ExecutableCommand<DataExchangePlugin> {

    protected DataProvider dp;
    protected PermissionService permissionService;

    public RefreshPermissionCommand(DataExchangePlugin plugin, DataProvider dp, PermissionService permissionService) {
        super(plugin);
        this.dp = dp;
        this.permissionService = permissionService;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        if (args.length != 0) {
            return false;
        }
        permissionService.refreshAllPermissions();
        sender.sendMessage(ChatColor.GREEN + "Successfully refreshed permissions for all players");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String command, String... args) {
        return new LinkedList<>();
    }

    @Override
    public boolean isForPlayerOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "/pm refresh";
    }

    @Override
    public String getName() {
        return "refresh";
    }
}
