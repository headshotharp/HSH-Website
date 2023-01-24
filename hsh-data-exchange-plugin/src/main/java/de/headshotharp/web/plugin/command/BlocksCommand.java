package de.headshotharp.web.plugin.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class BlocksCommand extends ExecutableCommand<DataExchangePlugin> {

    private DataProvider dp;

    public BlocksCommand(DataExchangePlugin plugin, DataProvider dp) {
        super(plugin);
        this.dp = dp;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        if (sender instanceof Player player) {
            Optional<User> user = dp.user().findByPlayer(player);
            if (user.isPresent()) {
                player.sendMessage("Blocks broken: " + user.get().getBrokenBlocks());
                player.sendMessage("Blocks placed: " + user.get().getPlacedBlocks());
                return true;
            } else {
                player.sendMessage("You are not in database!");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String command, String... args) {
        return new LinkedList<>();
    }

    @Override
    public boolean isForPlayerOnly() {
        return true;
    }

    @Override
    public String usage() {
        return "/blocks";
    }

    @Override
    public String getName() {
        return "blocks";
    }
}
