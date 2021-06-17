package com.glitchsoftware.monitor.task.impl.retail;

import com.glitchsoftware.monitor.product.impl.WalmartProduct;
import com.glitchsoftware.monitor.task.Task;
import com.glitchsoftware.monitor.task.callback.MonitorCallback;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public class WalmartTask extends Task {

    private WalmartProduct reloadProduct;
    private WalmartProduct cachedItem;

    public WalmartTask(MonitorCallback callback, String sku) {
        super(callback, sku);

        try {
            this.cachedItem = parseWalmart(sku);
        } catch (Exception e) {
            callback.onFailure("Failed to parse product");
        }
    }

    @Override
    public void run() {
        while (isStarted()) {
            try {
                reloadProduct = parseWalmart(getSku());

                if(reloadProduct != null) {
                    if(!reloadProduct.getStatus().equals(cachedItem.getStatus())) {
                        if(reloadProduct.getStatus().equalsIgnoreCase("IN_STOCK")) {
                            getCallback().onProductInStock(reloadProduct);

                            //rinse and reset
                            cachedItem = reloadProduct;
                            reloadProduct = null;
                        }
                    }
                }
            } catch (Exception e) {
                getCallback().onFailure("Failed to parse product");
            }
        }
    }

    public WalmartProduct parseWalmart(String sku) throws Exception {

        final Request request = new Request.Builder()
                .url(String.format("https://www.walmart.com/terra-firma/item/%s", sku))
                .get()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .header("Content-Type", "application/json")
                .header("cache-control", "no-store,no-cache,must-revalidate,proxy-revalidate,max-age=0")
                .build();

        try(Response response = getHttpClient().newCall(request).execute()) {
            if(response.request().url().toString().contains("blocked")) {
                // #TODO rotate proxy
                return parseWalmart(sku);
            }

            if(response.code() == 200) {
                final JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();

                final JsonObject payloadObject = jsonObject.getAsJsonObject("payload");
                final String primaryProduct = payloadObject.get("primaryProduct").getAsString();

                final JsonObject productObject = payloadObject.getAsJsonObject(primaryProduct);
                final String selectedImageName = productObject.getAsJsonArray("images").get(0).getAsString();

                final JsonObject imagesObject = payloadObject.getAsJsonObject("images").getAsJsonObject(selectedImageName);
                final String selectedImage = imagesObject.getAsJsonObject("assetSizeUrls").get("IMAGE_SIZE_180").getAsString();

                //#TODO do new endpoint
                final String productName = productObject.get("productName").getAsString();
                final String productUrl = String.format("https://www.walmart.com%s", productObject.get("canonicalUrl").getAsString());
                final String status = productObject.get("availabilityStatus").getAsString();
                final String imageUrl = productObject.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
                final double price = productObject.get("priceMap").getAsJsonObject().get("CURRENT").getAsJsonObject().get("price").getAsDouble();

                return new WalmartProduct(productName, imageUrl, productUrl, sku, status, "Walmart", price);
            } else {
                System.out.println("Got " + response.code());
            }
        }


        return null;
    }
}
