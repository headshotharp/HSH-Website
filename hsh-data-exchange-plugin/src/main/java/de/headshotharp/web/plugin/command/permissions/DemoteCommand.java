package de.headshotharp.web.plugin.command.permissions;

import de.headshotharp.web.plugin.DataExchangePlugin;
import de.headshotharp.web.plugin.command.permissions.generic.AbstractPromoteDemoteCommand;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import de.headshotharp.web.plugin.permissions.PermissionService;

public class DemoteCommand extends AbstractPromoteDemoteCommand {

    public DemoteCommand(DataExchangePlugin plugin, DataProvider dp, PermissionService permissionService) {
        super(plugin, dp, permissionService);
    }

    @Override
    public String getName() {
        return CMD_PROMOTE;
    }
}
