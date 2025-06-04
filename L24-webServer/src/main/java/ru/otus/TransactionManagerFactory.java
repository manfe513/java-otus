package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.from_orm_hw.core.repository.HibernateUtils;
import ru.otus.from_orm_hw.core.sessionmanager.TransactionManager;
import ru.otus.from_orm_hw.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.from_orm_hw.crm.model.Address;
import ru.otus.from_orm_hw.crm.model.Client;
import ru.otus.from_orm_hw.crm.model.Phone;
import ru.otus.model.User;

public class TransactionManagerFactory {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static TransactionManager initTransactionManager() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class, User.class);

        return new TransactionManagerHibernate(sessionFactory);
    }
}
