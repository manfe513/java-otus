package ru.otus.dao;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.from_orm_hw.core.repository.DataTemplate;
import ru.otus.from_orm_hw.core.sessionmanager.TransactionManager;
import ru.otus.model.User;

public class DbUserDao implements UserDao {

    private final Logger logger = LoggerFactory.getLogger(DbUserDao.class);

    private final TransactionManager tm;
    private final DataTemplate<User> dataTemplate;

    public DbUserDao(TransactionManager tm, DataTemplate<User> dataTemplate) {
        this.tm = tm;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Optional<User> findById(long id) {
        return tm.doInReadOnlyTransaction(session -> {
            var userOptional = dataTemplate.findById(session, id);
            logger.info("user: {}", userOptional);
            return userOptional;
        });
    }

    @Override
    public Optional<User> findRandomUser() {

        return tm.doInTransaction(session -> {
            User userNullable = session.createQuery("FROM User ORDER BY random()", User.class)
                    .setMaxResults(1)
                    .uniqueResult();

            return Optional.ofNullable(userNullable);
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return tm.doInReadOnlyTransaction(session -> {
            User usr = session.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();

            return Optional.ofNullable(usr);
        });
    }

    @Override
    public Optional<User> createUser(final User user) {
        return tm.doInTransaction(session -> {
            User usr = dataTemplate.insert(session, user);
            return Optional.ofNullable(usr);
        });
    }
}
