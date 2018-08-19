package com.shopifyObjects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SProduct {
    private long id;
    private String handle;
    private String title;
    private String tags;
    private String vendor;
    private String graphql_api_id;

    private String sku;
    private String price;
    private long quantity;
    private double weight;

    public SProduct() {

    }

    public SProduct(JSONObject product) {
        if (product == null) {
            System.out.println("null JSONObect.");
            return;
        }
        if (product.get("id").equals(null) || product.get("id").equals("")) {
            System.out.println("No products found in JSONObect.");
            return;
        }
        this.id = (long) product.get("id");
        this.handle = (String) product.get("handle");
        this.title = (String) product.get("title");
        this.tags = (String) product.get("tags");
        this.vendor = (String) product.get("vendor");
        this.graphql_api_id = (String) product.get("admin_graphql_api_id");

        JSONArray variants = (JSONArray) product.get("variants");
        for (Object variant : variants) {
            JSONObject variantObj = (JSONObject) variant;
            this.sku = (String) variantObj.get("sku");
            this.price = (String) variantObj.get("price");
            this.quantity = (long) variantObj.get("inventory_quantity");
            this.weight = (double) variantObj.get("weight");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getGraphql_api_id() {
        return graphql_api_id;
    }

    public void setGraphql_api_id(String graphql_api_id) {
        this.graphql_api_id = graphql_api_id;
    }
}