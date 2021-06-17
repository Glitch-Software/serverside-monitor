package com.glitchsoftware.monitor.product.impl;

import com.glitchsoftware.monitor.product.Product;
import lombok.AllArgsConstructor;

/**
 * @author Brennan
 * @since 6/12/2021
 **/
@AllArgsConstructor
public class NewEggProduct implements Product {
    private final String sku, name, image, url;
    private final double price;
    private final boolean inStock;

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
        return url;
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
        return "NewEgg";
    }

    public boolean isInStock() {
        return inStock;
    }

//    @Override
//    public String toString() {
//        return "NewEggProduct{" +
//                "sku='" + sku + '\'' +
//                ", name='" + name + '\'' +
//                ", image='" + image + '\'' +
//                ", url='" + url + '\'' +
//                ", price=" + price +
//                ", inStock=" + inStock +
//                '}';
//    }
}
