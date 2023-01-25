package de.headshotharp.web.plugin.command.permissions;

import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.service.PermissionService;

public class PromoteCommand extends AbstractPromoteDemoteCommand {

    public PromoteCommand(DataExchangePlugin plugin, DataProvider dp, PermissionService permissionService) {
        super(plugin, dp, permissionService);
    }

    @Override
    public String getName() {
        return CMD_DEMOTE;
    }
}
