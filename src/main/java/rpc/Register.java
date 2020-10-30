package rpc;

import database.MySQLConnection;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject input = new JSONObject(IOUtils.toString(request.getReader()));
//        String userIdtest = request.getParameter("user_id");
        String userId = input.getString("user_id");
        String password = input.getString("password");
        String firstname = input.getString("first_name");
        String lastname = input.getString("last_name");

        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject();
        if (connection.addUser(userId, password, firstname, lastname)) {
            obj.put("status", "OK");
        } else {
            obj.put("status", "User already exists!");
        }
        connection.close();
        RpcHelper.writeJsonObject(response, obj);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
