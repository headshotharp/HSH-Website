package de.headshotharp.web.server.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import de.headshotharp.web.database.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
