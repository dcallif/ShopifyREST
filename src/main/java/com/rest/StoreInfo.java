package com.rest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StoreInfo {
    private static String SHOPIFY_STORE = "Set me!";
    private static String TOKEN = "Set me!";

    public static void getBasicStoreInfo(String jsonBody) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(jsonBody);
            JSONObject data = (JSONObject) obj;
            JSONObject shopData = (JSONObject) data.get("data");
            JSONObject shop = (JSONObject) shopData.get("shop");
            String shopName = shop.get("name").toString();
            JSONObject domainInfo = (JSONObject) shop.get("primaryDomain");
            String shopUrl = domainInfo.get("url").toString();

            System.out.println(String.format("Store name: %s, Store URL: %s", shopName, shopUrl));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void setShopifyProperties() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/StoreInfo.properties"), StandardCharsets.UTF_8);
            if (lines.size() == 2) {
                SHOPIFY_STORE = lines.get(0).substring(lines.get(0).indexOf(":") + 1, lines.get(0).length());
                TOKEN = lines.get(1).substring(lines.get(1).indexOf(":") + 1, lines.get(1).length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            setShopifyProperties();
            if (SHOPIFY_STORE.equals("Set me!") || TOKEN.equals("Set me!")) return;
            URL url = new URL(SHOPIFY_STORE + "/api/graphql");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/graphql");
            conn.setRequestProperty("X-Shopify-Storefront-Access-Token", TOKEN);
            conn.setDoOutput(true);

            String input = "{\n" +
                    "  shop {\n" +
                    "    name\n" +
                    "    primaryDomain {\n" +
                    "      url\n" +
                    "      host\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String response = "", s;
            while ((s = br.readLine()) != null) {
                response += s;
            }
            conn.disconnect();
            getBasicStoreInfo(response);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}