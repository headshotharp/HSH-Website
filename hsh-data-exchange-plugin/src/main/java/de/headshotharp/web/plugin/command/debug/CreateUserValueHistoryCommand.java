package de.headshotharp.web.plugin.command.debug;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.service.DayChangeService;

public class CreateUserValueHistoryCommand extends ExecutableCommand<DataExchangePlugin> {

    private DayChangeService dayChangeService;

    public CreateUserValueHistoryCommand(DataExchangePlugin plugin, DayChangeService dayChangeService) {
        super(plugin);
        this.dayChangeService = dayChangeService;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        int amount = dayChangeService.saveUserHistory();
        sender.sendMessage(String.format("Created user history for %d users", amount));
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
        return "/hsh createuservaluehistory";
    }

    @Override
    public String getName() {
        return "createuservaluehistory";
    }
}
