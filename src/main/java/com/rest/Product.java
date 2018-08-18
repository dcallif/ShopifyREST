package com.rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Product {
    public static void main(String[] args) {
        byte[] someByteArray = "87eaf666840cc9033a6e3082bcfd71f0:62d4f48881010fa4ded36ecbb8048022".getBytes();
        String encoded = Base64.getEncoder().withoutPadding().encodeToString(someByteArray);
        String finalAuthorization = "Basic " + encoded;
        try {
            URL url = new URL("https://tycooninternational.myshopify.com/admin/products.json?fields=id,handle,title,tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", finalAuthorization);

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String response = "", s;
            while ((s = br.readLine()) != null) {
                response += s;
            }
            conn.disconnect();

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONObject productObj = (JSONObject) obj;
            JSONArray productsArr = (JSONArray) productObj.get("products");
            for (Object o : productsArr) {
                System.out.println(o.toString());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}