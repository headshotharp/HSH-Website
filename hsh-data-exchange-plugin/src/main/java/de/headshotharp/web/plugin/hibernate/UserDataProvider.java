package de.headshotharp.web.plugin.hibernate;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.hibernate.SessionFactory;

import de.headshotharp.plugin.hibernate.GenericDataProvider;
import de.headshotharp.web.database.User;

public class UserDataProvider extends GenericDataProvider<User> {

    public UserDataProvider(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public void createUser(Player player) {
        User user = User.builder().username(player.getName()).uuid(player.getUniqueId().toString()).build();
        persist(user);
    }

    public List<User> findAllUsers() {
        return findAll();
    }

    public Optional<User> findByPlayer(Player player) {
        return findByPredicate((builder, criteria, userRef, predicates) -> {
            predicates.add(builder.equal(userRef.get("uuid"), player.getUniqueId().toString()));
        });
    }

    public Optional<User> findByUsername(String username) {
        return findByPredicate((builder, criteria, userRef, predicates) -> {
            predicates.add(builder.equal(userRef.get("username"), username));
        });
    }

    public Optional<User> findByUuid(String uuid) {
        return findByPredicate((builder, criteria, userRef, predicates) -> {
            predicates.add(builder.equal(userRef.get("uuid"), uuid));
        });
    }

    public Optional<User> findByUsernameAndUuid(String username, String uuid) {
        return findByPredicate((builder, criteria, userRef, predicates) -> {
            predicates.add(builder.equal(userRef.get("username"), username));
            predicates.add(builder.equal(userRef.get("uuid"), uuid));
        });
    }

    public void incrementPlacedBlocks(Player player) {
        execInTransaction(session -> {
            String hqlUpdate = "UPDATE HSH_USER u SET u.placedBlocks = u.placedBlocks + 1 WHERE u.uuid = :uuid";
            return session.createMutationQuery(hqlUpdate).setParameter("uuid", player.getUniqueId().toString())
                    .executeUpdate();
        });
    }

    public void incrementBrokenBlocks(Player player) {
        execInTransaction(session -> {
            String hqlUpdate = "UPDATE HSH_USER u SET u.brokenBlocks = u.brokenBlocks + 1 WHERE u.uuid = :uuid";
            return session.createMutationQuery(hqlUpdate).setParameter("uuid", player.getUniqueId().toString())
                    .executeUpdate();
        });
    }
}
