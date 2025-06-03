package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";

    private static final String DEFAULT_PASSWORD = "111";

    private final transient UserDao userDao;
    private final transient TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        var randomUserOpt = userDao.findRandomUser();

        if (randomUserOpt.isPresent()) {
            paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, randomUserOpt.get());
        } else {
            User sampleUser = new User(1L, "Пользователь1", "user1", DEFAULT_PASSWORD);
            paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, sampleUser);
        }

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}
