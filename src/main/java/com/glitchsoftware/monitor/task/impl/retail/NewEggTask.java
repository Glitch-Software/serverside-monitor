package com.glitchsoftware.monitor.task.impl.retail;

import com.glitchsoftware.monitor.product.impl.NewEggProduct;
import com.glitchsoftware.monitor.product.impl.WalmartProduct;
import com.glitchsoftware.monitor.task.Task;
import com.glitchsoftware.monitor.task.callback.MonitorCallback;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public class NewEggTask extends Task {

    private NewEggProduct reloadProduct;
    private NewEggProduct cachedProduct;

    public NewEggTask(MonitorCallback callback, String sku) {
        super(callback, sku);

        this.cachedProduct = parseProduct();
    }

    @Override
    public void run() {
        while (isStarted()) {
            try {
                reloadProduct = parseProduct();

                if(reloadProduct != null) {

                    System.out.println(cachedProduct.isInStock() == reloadProduct.isInStock());
                    if(cachedProduct.isInStock() == reloadProduct.isInStock())
                        getCallback().onProductInStock(reloadProduct);

                    //rinse and reset
                    cachedProduct = reloadProduct;
                    reloadProduct = null;
                } else {
                    //ROTATE PROXY
                }
            } catch (Exception e) {
                getCallback().onFailure("Failed to parse product");
            }
        }
    }

    private NewEggProduct parseProduct() {

        try {
            final Request request = new Request.Builder()
                    .url(String.format("https://www.newegg.com/product/api/ProductRealtime?ItemNumber=%s&recaptcha=pass", getSku()))
                    .get()
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                    .header("Content-Type", "application/json")
                    .header("cache-control", "no-store,no-cache,must-revalidate,proxy-revalidate,max-age=0")
                    .build();


            try(Response response = getHttpClient().newCall(request).execute()) {

                if(response.code() == 200) {
                    final String body = response.body().string();

                    System.out.println(body);

                    if(body.contains("<!DOCTYPE html>")) {
                        System.out.println("rotate");
                        return null;
                    }

                    final JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                    final JsonObject productObject = jsonObject.get("MainItem").getAsJsonObject();
                    final JsonObject descriptionObject = productObject.getAsJsonObject("Description");
                    final JsonObject imageObject = productObject.getAsJsonObject("Image");

                    final String image = imageObject.getAsJsonObject("Normal").get("ImageName").getAsString();

                    final String imageUrl = String.format("https://c1.neweggimages.com/ProductImageCompressAll1280/%s", image);

                    final String urlKeyWords = descriptionObject.get("UrlKeywords").getAsString();

                    final String url = String.format("https://www.newegg.com/%s/p/%s", urlKeyWords, "N82E16819113567");

                    final double cost = productObject.get("FinalPrice").getAsDouble();

                    return new NewEggProduct(getSku(), descriptionObject.get("Title").getAsString(),  imageUrl, url, cost, productObject.get("Instock").getAsBoolean());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
