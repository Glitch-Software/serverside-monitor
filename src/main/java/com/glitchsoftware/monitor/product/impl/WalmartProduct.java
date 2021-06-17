package com.glitchsoftware.monitor.product.impl;

import com.glitchsoftware.monitor.product.Product;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public class WalmartProduct implements Product {

    private String name, image, link, sku, site, status;
    private double price;

    public WalmartProduct(String name, String image, String link, String sku, String site, String status, double price) {
        this.name = name;
        this.image = image;
        this.link = link;
        this.sku = sku;
        this.site = site;
        this.status = status;
        this.price = price;
    }

    public String getStatus() {
        return status;
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

    @Override
    public String toString() {
        return "WalmartProduct{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", sku='" + sku + '\'' +
                ", site='" + site + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                '}';
    }
}
