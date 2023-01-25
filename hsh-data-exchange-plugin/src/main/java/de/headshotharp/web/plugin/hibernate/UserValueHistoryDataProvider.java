package de.headshotharp.web.plugin.hibernate;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.headshotharp.plugin.hibernate.GenericDataProvider;
import de.headshotharp.web.database.UserValueHistory;

public class UserValueHistoryDataProvider extends GenericDataProvider<UserValueHistory> {

    public UserValueHistoryDataProvider(Session currentSession) {
        super(currentSession);
    }

    public UserValueHistoryDataProvider(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Class<UserValueHistory> getEntityClass() {
        return UserValueHistory.class;
    }

    public Optional<UserValueHistory> findLatest() {
        return findByPredicate((builder, criteria, root, predicates) -> {
            criteria.orderBy(builder.desc(root.get("created")));
        }, 0, 1);
    }
}
