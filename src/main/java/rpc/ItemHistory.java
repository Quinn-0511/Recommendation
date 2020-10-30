package rpc;

import database.MySQLConnection;
import entity.Item;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebServlet("/history")
public class ItemHistory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        MySQLConnection connection = new MySQLConnection();
        // read the body of request. This request includes many information, like the item information.
        JSONObject input = new JSONObject(IOUtils.toString(request.getReader()));
        String userId = input.getString("user_id");
        Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

        connection.setFavoriteItems(userId, item);
        connection.close();
        RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        String userId = request.getParameter("user_id");
        MySQLConnection connection = new MySQLConnection();
        Set<Item> items = connection.getFavoriteItems(userId);
        connection.close();

        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", true);
            array.put(obj);
        }
        RpcHelper.writeJsonArray(response, array);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        MySQLConnection connection = new MySQLConnection();
        JSONObject input = new JSONObject(IOUtils.toString(request.getReader()));
        String userId = input.getString("user_id");
        Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

        connection.unsetFavoriteItems(userId, item.getItemId());
        connection.close();
        RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
    }
}
