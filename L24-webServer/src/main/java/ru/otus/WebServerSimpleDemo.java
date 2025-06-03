package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.from_orm_hw.core.repository.DataTemplateHibernate;
import ru.otus.from_orm_hw.core.repository.HibernateUtils;
import ru.otus.from_orm_hw.core.sessionmanager.TransactionManager;
import ru.otus.from_orm_hw.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.from_orm_hw.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.from_orm_hw.crm.model.Address;
import ru.otus.from_orm_hw.crm.model.Client;
import ru.otus.from_orm_hw.crm.model.Phone;
import ru.otus.from_orm_hw.crm.service.DbServiceClientImpl;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerSimple;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerSimpleDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        TransactionManager tm = initTransactionManager();
        ///
        var dbServiceClient = new DbServiceClientImpl(tm, new DataTemplateHibernate<>(Client.class));
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clients = dbServiceClient.findAll();
        clients.forEach(c -> System.out.println(c));

        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UsersWebServer usersWebServer = new UsersWebServerSimple(WEB_SERVER_PORT, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static TransactionManager initTransactionManager() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        return new TransactionManagerHibernate(sessionFactory);
    }
}
