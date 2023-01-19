package de.headshotharp.web.plugin.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.headshotharp.plugin.hibernate.GenericDataProvider;
import de.headshotharp.web.database.Role;

public class RoleDataProvider extends GenericDataProvider<Role> {

    public RoleDataProvider(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public RoleDataProvider(Session session) {
        super(session);
    }

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    public List<Role> findAll() {
        return findAllByPredicate(
                (builder, criteria, root, predicates) -> criteria.orderBy(builder.desc(root.get("power"))));
    }

    public Optional<Role> findByName(String name) {
        return findByPredicate(
                (builder, criteria, root, predicates) -> predicates.add(builder.equal(root.get("name"), name)));
    }

    public Optional<Role> findByLowestPower() {
        return findByPredicate(
                (builder, criteria, root, predicates) -> criteria.orderBy(builder.asc(root.get("power"))), 0, 1);
    }

    public Optional<Role> findByNextPower(int currentPower) {
        return findByPredicate(
                (builder, criteria, root, predicates) -> {
                    criteria.orderBy(builder.asc(root.get("power")));
                    predicates.add(builder.greaterThan(root.get("power"), currentPower));
                }, 0, 1);
    }

    public Optional<Role> findByPreviousPower(int currentPower) {
        return findByPredicate(
                (builder, criteria, root, predicates) -> {
                    criteria.orderBy(builder.desc(root.get("power")));
                    predicates.add(builder.lessThan(root.get("power"), currentPower));
                }, 0, 1);
    }

    public List<Role> findAllByPowerLowerOrEqualTo(int currentPower) {
        return findAllByPredicate(
                (builder, criteria, root, predicates) -> {
                    criteria.orderBy(builder.asc(root.get("power")));
                    predicates.add(builder.lessThanOrEqualTo(root.get("power"), currentPower));
                });
    }
}
