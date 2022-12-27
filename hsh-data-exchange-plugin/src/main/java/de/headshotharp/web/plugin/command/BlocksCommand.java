package de.headshotharp.web.plugin.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class BlocksCommand implements CommandExecutor, TabCompleter {

    private DataProvider dp;

    public BlocksCommand(DataProvider dp) {
        this.dp = dp;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        return new LinkedList<>();
    }
}
