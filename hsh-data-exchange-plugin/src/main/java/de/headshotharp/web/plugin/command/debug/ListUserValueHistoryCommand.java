package de.headshotharp.web.plugin.command.debug;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.database.UserValueHistory;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class ListUserValueHistoryCommand extends ExecutableCommand<DataExchangePlugin> {

    private DataProvider dp;

    public ListUserValueHistoryCommand(DataExchangePlugin plugin, DataProvider dp) {
        super(plugin);
        this.dp = dp;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        List<UserValueHistory> history = dp.userValueHistory().findAll();
        for (UserValueHistory entry : history) {
            String template = """
                    User: %s
                    Money: %.2f
                    Placed Blocks: %d
                    Broken Blocks: %d
                    """;
            String msg = String.format(template,
                    entry.getUser().getUsername(),
                    entry.getMoney(),
                    entry.getPlacedBlocks(),
                    entry.getBrokenBlocks());
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
        return "/hsh listuservaluehistory";
    }

    @Override
    public String getName() {
        return "listuservaluehistory";
    }
}
