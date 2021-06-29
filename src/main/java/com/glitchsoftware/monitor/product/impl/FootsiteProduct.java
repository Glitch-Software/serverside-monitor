package com.glitchsoftware.monitor.product.impl;

import com.glitchsoftware.monitor.product.Product;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Brennan
 * @since 6/27/21
 **/
public class FootsiteProduct implements Product {
    private final String name, sku, image, link, site;
    private double price;

    private boolean loaded;

    private final List<String> sizesInStock = new LinkedList<>();

    public FootsiteProduct(String name, String sku, String image, String link, String site, double price) {
        this.name = name;
        this.sku = sku;
        this.image = image;
        this.link = link;
        this.site = site;
        this.price = price;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public List<String> getSizesInStock() {
        return sizesInStock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getSKU() {
        return sku;
    }

    @Override
    public String getSite() {
        return site;
    }

}
