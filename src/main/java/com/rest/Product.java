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
import java.util.Base64;
import java.util.List;

public class Product {

    /**
     * @return empty String if file not found or incorrect number of input properties
     */
    private String getShopifyPropertiesEncoded() {
        String properties = "";
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/ShopifyStore.properties"), StandardCharsets.UTF_8);
            if (lines.size() == 2) {
                properties += lines.get(0).substring(lines.get(0).indexOf("=") + 1, lines.get(0).length());
                properties += ":";
                properties += lines.get(1).substring(lines.get(1).indexOf("=") + 1, lines.get(1).length());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        if (!(properties.equals(""))) {
            byte[] someByteArray = properties.getBytes();
            String encoded = Base64.getEncoder().withoutPadding().encodeToString(someByteArray);
            String finalAuthorization = "Basic " + encoded;
            return finalAuthorization;
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
            for (Object o : productsArr) {
                System.out.println(o.toString());
            }
            return productsArr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Product p = new Product();
        String properties = p.getShopifyPropertiesEncoded();
        if (properties.equals("")) System.out.println("Could not get properties");
        try {
            URL url = new URL("https://tycooninternational.myshopify.com/admin/products.json?fields=id,handle,title,tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", properties);

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String response = "", s;
            while ((s = br.readLine()) != null) {
                response += s;
            }
            conn.disconnect();

            p.convertResponseToProductsArray(response);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}