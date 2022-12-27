package de.headshotharp.web.plugin.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import de.headshotharp.plugin.hibernate.HibernateUtils;
import de.headshotharp.plugin.hibernate.config.HibernateConfig;

public class DataProvider {

    private UserDataProvider userDataProvider;

    public DataProvider(SessionFactory sessionFactory) {
        userDataProvider = new UserDataProvider(sessionFactory);
    }

    public DataProvider(HibernateConfig hibernateConfig, Class<?> baseClass) {
        this(new HibernateUtils(hibernateConfig, baseClass).createSessionFactory());
    }

    public DataProvider(HibernateConfig hibernateConfig, List<Class<?>> daoClasses) {
        this(new HibernateUtils(hibernateConfig, daoClasses).createSessionFactory());
    }

    public UserDataProvider user() {
        return userDataProvider;
    }
}
