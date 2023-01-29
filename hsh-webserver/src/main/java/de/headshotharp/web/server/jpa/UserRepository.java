package de.headshotharp.web.server.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import de.headshotharp.web.database.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
