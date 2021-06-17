package com.glitchsoftware.monitor.task.callback;

import com.glitchsoftware.monitor.product.Product;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public interface MonitorCallback {

    void onProductInStock(Product product);

    void onFailure(String reason);
}
