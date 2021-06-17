package com.glitchsoftware.monitor.socket.notification.impl;

import com.glitchsoftware.monitor.product.Product;
import com.glitchsoftware.monitor.socket.notification.Notification;
import com.google.gson.JsonObject;

/**
 * @author Brennan
 * @since 6/17/2021
 **/
public class ProductNotification implements Notification {

    private Product product;

    public ProductNotification(Product product) {
        this.product = product;
    }

    @Override
    public JsonObject toJSON() {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", product.getName());
        jsonObject.addProperty("image", product.getImage());
        jsonObject.addProperty("link", product.getLink());
        jsonObject.addProperty("price", product.getPrice());
        jsonObject.addProperty("sku", product.getSKU());
        jsonObject.addProperty("site", product.getSite());

        return jsonObject;
    }
}
