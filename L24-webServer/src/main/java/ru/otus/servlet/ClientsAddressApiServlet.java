package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import ru.otus.dao.ClientDao;
import ru.otus.model.RequestUpdateAddressModel;

@SuppressWarnings({"java:S1989"})
public class ClientsAddressApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient ClientDao clientsDao;
    private final transient Gson gson;

    public ClientsAddressApiServlet(ClientDao clientsDao, Gson gson) {
        this.clientsDao = clientsDao;
        this.gson = gson;
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        var requestModel = gson.fromJson(sb.toString(), RequestUpdateAddressModel.class);

        var clientOptional = clientsDao.updateAddress(requestModel.getClientId(), requestModel.getAddress());

        if (clientOptional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        ServletOutputStream out = response.getOutputStream();
        //        out.print(gson.toJson(clientOptional));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
