package de.headshotharp.web.plugin.hibernate;

import java.util.LinkedList;
import java.util.List;

import de.headshotharp.plugin.hibernate.DataProviderBase;
import de.headshotharp.plugin.hibernate.config.HibernateConfig;
import de.headshotharp.plugin.hibernate.dao.DataAccessObject;
import de.headshotharp.web.database.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DataProvider extends DataProviderBase {

    public DataProvider(HibernateConfig hibernateConfig, Class<?> baseClass) {
        super(hibernateConfig, baseClass);
    }

    public DataProvider(HibernateConfig hibernateConfig, List<Class<? extends DataAccessObject>> daoClasses) {
        super(hibernateConfig, daoClasses);
    }

    public List<User> findAllUsers() {
        return getInTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            return session.createQuery(criteria).getResultList();
        });
    }

    public List<User> findUserByUsername(String username) {
        return getInTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRef = criteria.from(User.class);
            // set predicates
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(builder.equal(userRef.get("username"), username));
            // add predicates
            criteria.where(builder.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(criteria).getResultList();
        });
    }
}
