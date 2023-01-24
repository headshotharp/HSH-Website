package de.headshotharp.web.plugin.command.permissions;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.database.Role;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class RolesCommand extends ExecutableCommand<DataExchangePlugin> {

    private DataProvider dp;

    public RolesCommand(DataExchangePlugin plugin, DataProvider dp) {
        super(plugin);
        this.dp = dp;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        if (args.length == 0) {
            List<Role> roles = dp.role().findAll();
            if (roles.isEmpty()) {
                sender.sendMessage("There are no roles configured");
            } else {
                String msg = "Roles:\n"
                        + String.join("\n",
                                roles.stream().map(r -> String.format("%1$4s", r.getPower()) + ": " + r.getName())
                                        .toList());
                sender.sendMessage(msg);
            }
        } else if (args.length == 1) {
            Role role = dp.role().findByName(args[0]).orElse(null);
            if (role == null) {
                sender.sendMessage(ChatColor.RED + String.format("No Role with name '%s' found", args[0]));
            } else {
                String permissions = "- " + String.join("\n -", role.getPermissions());
                String template = """
                        Role Info:
                        Name:        %s
                        Power:       %d
                        ID:          %d
                        Description: %s
                        Prefix:      %s
                        Permissions:
                        %s
                        """;
                String msg = String.format(template, role.getName(), role.getPower(), role.getId(),
                        role.getDescription(),
                        role.getPrefix(), permissions);
                sender.sendMessage(msg);
            }
        } else {
            return false;
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
        return "/pm roles [role]";
    }

    @Override
    public String getName() {
        return "roles";
    }
}
