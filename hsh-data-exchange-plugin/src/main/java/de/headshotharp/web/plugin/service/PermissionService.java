package de.headshotharp.web.plugin.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.Session;

import de.headshotharp.web.database.Role;
import de.headshotharp.web.database.User;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PermissionService {

    private final JavaPlugin plugin;
    private final DataProvider dp;

    private Map<Player, PermissionAttachment> globalPermissions = new HashMap<>();
    private Map<Player, String> prefixes = new HashMap<>();

    public String getPlayerPrefix(Player player) {
        return prefixes.getOrDefault(player, "");
    }

    public void setPlayerPrefix(Player player, String prefix) {
        prefixes.put(player, prefix);
    }

    public void resetPlayerPrefix(Player player) {
        prefixes.remove(player);
    }

    public void refreshAllPermissions() {
        try (Session session = dp.openTransaction()) {
            refreshAllPermissions(session);
            dp.commitTransaction(session);
        }
    }

    public void refreshAllPermissions(Session session) {
        List<Role> roles = dp.role(session).findAll();
        Map<String, Role> uuidRoleMap = new HashMap<>();
        for (Role role : roles) {
            for (User user : role.getUsers()) {
                uuidRoleMap.put(user.getUuid(), role);
            }
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Role role = uuidRoleMap.get(player.getUniqueId().toString());
            if (role != null && role.getPermissions() != null) {
                setPermissions(player, role.getPermissions());
            } else {
                resetPermissions(player);
            }
        }
    }

    public void setPermissions(Player player) {
        try (Session session = dp.openTransaction()) {
            setPermissions(player, session);
            dp.commitTransaction(session);
        }
    }

    public void setPermissions(Player player, Session session) {
        User user = dp.user(session).findByPlayer(player).orElse(null);
        if (user != null) {
            List<String> permissions = new LinkedList<>();
            if (user.getRole() != null) {
                setPlayerPrefix(player, user.getRole().getPrefix());
                List<Role> includedRoles = dp.role(session).findAllByPowerLowerOrEqualTo(user.getRole().getPower());
                includedRoles.stream().map(Role::getPermissions).forEach(permissions::addAll);
            }
            setPermissions(player, permissions);
        }
    }

    public void resetPermissions(Player player) {
        setPermissions(player, new LinkedList<>());
    }

    private void setPermissions(Player player, List<String> permissions) {
        // clean
        PermissionAttachment attachment = globalPermissions.get(player);
        if (attachment != null) {
            attachment.remove();
        }
        // create attachment
        attachment = player.addAttachment(plugin);
        globalPermissions.put(player, attachment);
        // attach permissions
        attachPermissions(attachment, permissions);
    }

    private void attachPermissions(PermissionAttachment attachment, List<String> permissions) {
        permissions.forEach(p -> attachment.setPermission(p, true));
    }
}
