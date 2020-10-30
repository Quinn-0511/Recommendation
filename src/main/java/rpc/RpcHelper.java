package rpc;

import entity.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RpcHelper {
    // Write a JSONArray to http response
    public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
        response.setContentType("application/json");
        response.getWriter().print(array);
    }

    // Write a JSONObject to http response
    public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
        response.setContentType("applicaiton/json");
        response.getWriter().print(obj);
    }

    // Convert a JSON object to Item object
    public static Item parseFavoriteItem(JSONObject favoriteItem) {
        Item.ItemBuilder builder = new Item.ItemBuilder();
        builder.setItemId(favoriteItem.getString("item_id"));
        builder.setName(favoriteItem.getString("name"));
        builder.setAddress(favoriteItem.getString("address"));
        builder.setUrl(favoriteItem.getString("url"));
        builder.setImageUrl(favoriteItem.getString("image_url"));

        Set<String> keywords = new HashSet<>();
        JSONArray array = favoriteItem.getJSONArray("keywords");
        for (int i = 0; i < array.length(); ++i) {
            keywords.add(array.getString(i));
        }
        builder.setKeywords(keywords);
        return builder.build();
    }

    // use private constructor to prohibit others to create a new RpcHelper object.


//        for (int i = 0; i < array.length(); i++) {
//        JSONObject object = array.getJSONObject(i);
//        // object -> item
//        Item item = new Item.ItemBuilder()
//                .setItemId(getStringFieldOrEmpty(object, "id"))
//                .setName(getStringFieldOrEmpty(object, "title"))
//                .setAddress(getStringFieldOrEmpty(object, "location"))
//                .setUrl(getStringFieldOrEmpty(object, "url"))
//                .setImageUrl(getStringFieldOrEmpty(object, "company_logo"))
//                .setKeywords(new HashSet<String>(keywords.get(i)))
//                .build();



}
