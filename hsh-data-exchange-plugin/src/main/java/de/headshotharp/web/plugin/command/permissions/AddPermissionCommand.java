package de.headshotharp.web.plugin.command.permissions;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.hibernate.Session;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.database.Role;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.service.PermissionService;

public class AddPermissionCommand extends ExecutableCommand<DataExchangePlugin> {

    protected DataProvider dp;
    protected PermissionService permissionService;

    public AddPermissionCommand(DataExchangePlugin plugin, DataProvider dp, PermissionService permissionService) {
        super(plugin);
        this.dp = dp;
        this.permissionService = permissionService;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        if (args.length != 2) {
            return false;
        }
        String permission = args[1];
        try (Session session = dp.openTransaction()) {
            String roleArg = args[0];
            Role role = dp.role(session).findByName(roleArg).orElse(null);
            if (role == null) {
                sender.sendMessage(ChatColor.RED + String.format("No role with name '%s' found", roleArg));
            } else {
                role.getPermissions().add(permission);
                dp.role(session).persist(role);
                permissionService.refreshAllPermissions(session);
                dp.commitTransaction(session);
                sender.sendMessage(ChatColor.GREEN
                        + String.format("Successfully added permission '%s' to role '%s'", permission, role.getName()));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String command, String... args) {
        if (args.length == 0 || (args.length == 1 && args[0].isEmpty())) {
            return dp.role().findAll().stream().map(Role::getName).toList();
        } else if (args.length == 1) {
            return dp.role().findByNameStartingWith(args[0]).stream().map(Role::getName).toList();
        }
        return new LinkedList<>();
    }

    @Override
    public boolean isForPlayerOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "/pm addpermission <role> <permission>";
    }

    @Override
    public String getName() {
        return "addpermission";
    }
}
