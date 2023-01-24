package de.headshotharp.web.plugin.command.permissions;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.hibernate.Session;

import de.headshotharp.web.database.Role;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.permissions.PermissionService;

public class RemovePermissionCommand extends AddPermissionCommand {

    public RemovePermissionCommand(DataExchangePlugin plugin, DataProvider dp, PermissionService permissionService) {
        super(plugin, dp, permissionService);
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
            } else if (role.getPermissions().contains(permission)) {
                role.getPermissions().remove(permission);
                dp.role(session).persist(role);
                permissionService.refreshAllPermissions(session);
                dp.commitTransaction(session);
                sender.sendMessage(ChatColor.GREEN + String
                        .format("Successfully removed permission '%s' from role '%s'", permission, role.getName()));
            } else {
                dp.commitTransaction(session);
                sender.sendMessage(ChatColor.GREEN + String.format("The role '%s' does not provide the permission '%s'",
                        role.getName(), permission));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String command, String... args) {
        if (args.length == 2) {
            Role role = dp.role().findByName(args[0]).orElse(null);
            if (role == null) {
                return new LinkedList<>();
            } else {
                return role.getPermissions().stream().filter(s -> s.startsWith(args[1])).toList();
            }
        } else {
            return super.onTabComplete(sender, command, args);
        }
    }

    @Override
    public String usage() {
        return "/pm removepermission <role> <permission>";
    }

    @Override
    public String getName() {
        return "removepermission";
    }
}
