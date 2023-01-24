package de.headshotharp.web.plugin.command.permissions;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hibernate.Session;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.database.Role;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.permissions.PermissionService;

public abstract class AbstractPromoteDemoteCommand extends ExecutableCommand<DataExchangePlugin> {

    protected static final String CMD_PROMOTE = "promote";
    protected static final String CMD_DEMOTE = "demote";

    private DataProvider dp;
    private PermissionService permissionService;

    protected AbstractPromoteDemoteCommand(DataExchangePlugin plugin, DataProvider dp,
            PermissionService permissionService) {
        super(plugin);
        this.dp = dp;
        this.permissionService = permissionService;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        if (args.length != 1) {
            return false;
        }
        String playerName = args[0];
        UUID playerUuid = getPlugin().getServer().getPlayerUniqueId(playerName);
        if (playerUuid == null) {
            sender.sendMessage(ChatColor.DARK_RED + String.format("Cannot find player %s", playerName));
            return true;
        }
        String uuid = playerUuid.toString();
        try (Session session = dp.openTransaction()) {
            User user = dp.user(session).findByUuid(uuid).orElse(null);
            if (user == null) {
                sender.sendMessage(ChatColor.DARK_RED + String.format("Player %s is not registered", playerName));
                return true;
            }
            if (user.getRole() == null) {
                // no role, give default role
                Role defaultRole = dp.role(session).findByLowestPower().orElse(null);
                if (defaultRole == null) {
                    sender.sendMessage(ChatColor.DARK_RED + "There are no roles configured");
                    return true;
                }
                user.setRole(defaultRole);
                dp.user(session).persist(user);
                permissionService.setPermissions(getPlugin().getServer().getPlayer(playerName), session);
                dp.commitTransaction(session);
                sender.sendMessage(ChatColor.GREEN + String.format("Player %s has been given the default role %s",
                        playerName, defaultRole.getName()));
                return true;
            } else {
                String action;
                Role nextRole;
                if (command.equalsIgnoreCase(CMD_PROMOTE)) {
                    action = "promoted";
                    nextRole = dp.role(session).findByNextPower(user.getRole().getPower()).orElse(null);
                } else {
                    action = "demoted";
                    nextRole = dp.role(session).findByPreviousPower(user.getRole().getPower()).orElse(null);
                }
                if (nextRole == null) {
                    sender.sendMessage(ChatColor.YELLOW + String.format("Player %s cannot be %s any further",
                            playerName, action));
                    return true;
                }
                user.setRole(nextRole);
                dp.user(session).persist(user);
                permissionService.setPermissions(getPlugin().getServer().getPlayer(playerName), session);
                dp.commitTransaction(session);
                sender.sendMessage(ChatColor.GREEN + String.format("Player %s has been %s to %s",
                        playerName, action, nextRole.getName()));
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String command, String... args) {
        if (args.length == 0) {
            return getPlugin().getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        } else if (args.length == 1) {
            return getPlugin().getServer().getOnlinePlayers().stream().map(Player::getName)
                    .filter(cmd -> cmd.startsWith(args[0]))
                    .toList();
        }
        return new LinkedList<>();
    }

    @Override
    public boolean isForPlayerOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "/pm <promote/demote> <player>";
    }
}
