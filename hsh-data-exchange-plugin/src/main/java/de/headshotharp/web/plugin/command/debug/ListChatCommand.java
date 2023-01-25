package de.headshotharp.web.plugin.command.debug;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.hibernate.Session;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;
import de.headshotharp.web.database.ChatEntry;
import de.headshotharp.web.database.ChatEntry.ChatType;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;

public class ListChatCommand extends ExecutableCommand<DataExchangePlugin> {

    private DataProvider dp;

    public ListChatCommand(DataExchangePlugin plugin, DataProvider dp) {
        super(plugin);
        this.dp = dp;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {

        try (Session session = dp.openTransaction()) {
            List<ChatEntry> chat = dp.chat(session).findAll();
            String msg = String.join("\n",
                    chat.stream().map(entry -> {
                        StringBuilder s = new StringBuilder();
                        String username = getSafeUsername(entry.getUser());
                        if (entry.getType() == ChatType.LOGIN) {
                            s.append(username);
                            s.append(" logged in");
                        } else if (entry.getType() == ChatType.LOGOUT) {
                            s.append(username);
                            s.append(" logged out");
                        } else if (entry.getType() == ChatType.SERVER) {
                            s.append("Server: ");
                            s.append(entry.getMsg());
                        } else if (entry.getType() == ChatType.MINECRAFT) {
                            s.append("[MC] ");
                            s.append(username);
                            s.append(": ");
                            s.append(entry.getMsg());
                        } else if (entry.getType() == ChatType.WEB) {
                            s.append("[Web] ");
                            s.append(username);
                            s.append(": ");
                            s.append(entry.getMsg());
                        }
                        return s.toString();
                    }).toList());
            sender.sendMessage(msg);
            return true;
        }
    }

    private String getSafeUsername(User user) {
        if (user != null && user.getUsername() != null) {
            return user.getUsername();
        }
        return "";
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
        return "/hsh chat";
    }

    @Override
    public String getName() {
        return "chat";
    }
}
