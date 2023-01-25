package de.headshotharp.web.plugin.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.headshotharp.plugin.hibernate.HibernateUtils;
import de.headshotharp.plugin.hibernate.config.HibernateConfig;

public class DataProvider {

    private SessionFactory sessionFactory;
    private UserDataProvider userDataProvider;
    private RoleDataProvider roleDataProvider;
    private UserValueHistoryDataProvider userValueHistoryDataProvider;

    public DataProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        userDataProvider = new UserDataProvider(sessionFactory);
        roleDataProvider = new RoleDataProvider(sessionFactory);
        userValueHistoryDataProvider = new UserValueHistoryDataProvider(sessionFactory);
    }

    public DataProvider(HibernateConfig hibernateConfig, Class<?> baseClass) {
        this(new HibernateUtils(hibernateConfig, baseClass).createSessionFactory());
    }

    public DataProvider(HibernateConfig hibernateConfig, List<Class<?>> daoClasses) {
        this(new HibernateUtils(hibernateConfig, daoClasses).createSessionFactory());
    }

    // support manual transaction handling

    public Session openTransaction() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }

    public void commitTransaction(Session session) {
        session.getTransaction().commit();
        session.close();
    }

    // -----------------------------------

    public UserDataProvider user() {
        return userDataProvider;
    }

    public UserDataProvider user(Session session) {
        return new UserDataProvider(session);
    }

    public RoleDataProvider role() {
        return roleDataProvider;
    }

    public RoleDataProvider role(Session session) {
        return new RoleDataProvider(session);
    }

    public UserValueHistoryDataProvider userValueHistory() {
        return userValueHistoryDataProvider;
    }

    public UserValueHistoryDataProvider userValueHistory(Session session) {
        return new UserValueHistoryDataProvider(session);
    }
}
