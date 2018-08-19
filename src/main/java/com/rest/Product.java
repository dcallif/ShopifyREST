package com.rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Product {

    /**
     * @return empty List if property file not found or to few parameters found
     */
    private List<String> getProperties() {
        List<String> properties = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/ShopifyStore.properties"), StandardCharsets.UTF_8);
            if (lines.size() > 2) {
                properties.add(lines.get(0).substring(lines.get(0).indexOf("=") + 1, lines.get(0).length()) + ":" + lines.get(1).substring(lines.get(1).indexOf("=") + 1, lines.get(1).length()));
                properties.add(lines.get(2).substring(lines.get(2).indexOf("=") + 1));
                properties.add(lines.get(3).substring(lines.get(3).indexOf("=") + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return properties;
        }
        return properties;
    }

    /**
     * @param response String representation of JSON response
     * @return null if bad formatting
     */
    private JSONArray convertResponseToProductsArray(String response) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(response);
            JSONObject productObj = (JSONObject) obj;
            JSONArray productsArr = (JSONArray) productObj.get("products");
            return productsArr;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Product p = new Product();
        List<String> properties = p.getProperties();
        if (properties.size() < 1) {
            System.out.println("Could not get properties");
            return;
        }
        byte[] someByteArray = properties.get(0).getBytes();
        String encoded = Base64.getEncoder().withoutPadding().encodeToString(someByteArray);
        String finalAuthorization = "Basic " + encoded;
        try {
            // Can add to url to reduce response size: String limitFields = "?fields=id,handle,title,tags";
            URL url = new URL(properties.get(1) + properties.get(2) + "");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", finalAuthorization);

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String response = "", s;
            while ((s = br.readLine()) != null) {
                response += s;
            }
            conn.disconnect();

            JSONArray productsArr = p.convertResponseToProductsArray(response);
            for (Object o : productsArr) {
                JSONObject productObj = (JSONObject) o;
                System.out.println(String.format("ID: %s, Handle: %s, Title: %s, Tags: %s", productObj.get("id"), productObj.get("handle"),
                        productObj.get("title"), "{" + productObj.get("tags") + "}"));
                JSONArray variants = (JSONArray) productObj.get("variants");
                for (Object variant : variants) {
                    JSONObject variantObj = (JSONObject) variant;
                    System.out.println(String.format("SKU: %s, Price: %s, Inventory: %s", variantObj.get("sku"), variantObj.get("price"),
                            variantObj.get("inventory_quantity")));
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}