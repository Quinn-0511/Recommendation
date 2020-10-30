package httpClient;

import com.monkeylearn.MonkeyLearnResponse;
import entity.Item.ItemBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import entity.Item;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class GitHubClient {
    // %s could be replaced.
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    private static final String DEFAULT_KEYWORD = "developer";

    public List<Item> search(double lat, double lon, String keyword) {

        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        try {
            // Solve the special character in keyword
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Build the url
        String url = String.format(URL_TEMPLATE, keyword, lat, lon);
        // create the client to send the url and receive the response
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {
            @Override
            public List<Item> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status != 200) {
//                    return new JSONArray(new JSONObject().put("Status Code is not 200 and is: ", status));
                    return new ArrayList<>();
                }
                HttpEntity entity = response.getEntity();
                if (entity == null) {
//                    return new JSONArray(new JSONObject().put("Entity is empty", "Empty"));
                    return new ArrayList<>();
                }
                String responseBody = EntityUtils.toString(entity);
                return getItemList(new JSONArray(responseBody));
            }
        };

        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Item> getItemList(JSONArray array) {
        List<Item> itemList = new ArrayList<>();
        List<String> descriptionList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String description = getStringFieldOrEmpty(array.getJSONObject(i), "description");
            if (description.equals("") || description.equals("\n")) {
                descriptionList.add(getStringFieldOrEmpty(array.getJSONObject(i), "title"));
            } else {
                descriptionList.add(description);
            }
        }
        String[] descriptionArray = descriptionList.toArray(new String[descriptionList.size()]);

        List<List<String>> keywords = MonkeyLearnClient.extractKeywords(descriptionArray);



        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            // object -> item
            Item item = new Item.ItemBuilder()
                    .setItemId(getStringFieldOrEmpty(object, "id"))
                    .setName(getStringFieldOrEmpty(object, "title"))
                    .setAddress(getStringFieldOrEmpty(object, "location"))
                    .setUrl(getStringFieldOrEmpty(object, "url"))
                    .setImageUrl(getStringFieldOrEmpty(object, "company_logo"))
                    .setKeywords(new HashSet<String>(keywords.get(i)))
                    .build();
//            ItemBuilder builder = new ItemBuilder();
//            builder.setItemId(getStringFieldOrEmpty(object, "id"));
//            builder.setName(getStringFieldOrEmpty(object, "title"));
//            builder.setAddress(getStringFieldOrEmpty(object, "location"));
//            builder.setUrl(getStringFieldOrEmpty(object, "url"));
//            builder.setImageUrl(getStringFieldOrEmpty(object, "company_logo"));
//            Item item = builder.build();

            itemList.add(item);

        }

        return itemList;

    }

    private String getStringFieldOrEmpty(JSONObject obj, String field) {
        return obj.isNull(field) ? "" : obj.getString(field);
    }


}

