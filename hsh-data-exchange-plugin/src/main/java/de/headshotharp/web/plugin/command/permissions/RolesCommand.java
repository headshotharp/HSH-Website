package de.headshotharp.web.plugin.command.permissions;

import java.util.LinkedList;
import java.util.List;

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
        if (args.length != 0) {
            return false;
        }
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
        return "/pm roles";
    }

    @Override
    public String getName() {
        return "roles";
    }
}
