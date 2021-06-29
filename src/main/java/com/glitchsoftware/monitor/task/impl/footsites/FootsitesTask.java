package com.glitchsoftware.monitor.task.impl.footsites;

import com.glitchsoftware.monitor.product.impl.FootsiteProduct;
import com.glitchsoftware.monitor.task.Task;
import com.glitchsoftware.monitor.task.callback.MonitorCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Date;
import java.util.UUID;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public class FootsitesTask extends Task {
    private final Footsites site;

    private FootsiteProduct cachedProduct;

    public FootsitesTask(MonitorCallback callback, Footsites site, String sku) {
        super(callback, sku);
        this.site = site;

        this.cachedProduct = parseProduct();
    }

    @Override
    public void run() {
        while (isStarted()) {
            FootsiteProduct reloadedProduct = parseProduct();

            if(reloadedProduct != null) {
                if(!reloadedProduct.getSizesInStock().equals(cachedProduct.getSizesInStock())) {
                    reloadedProduct.setLoaded(isProductLoaded());
                    getCallback().onProductInStock(reloadedProduct);
                }
            } else {
                rotateProxy();
                //ROTATE PROXY
            }

            System.out.println("reloading");
            cachedProduct = reloadedProduct;
        }
    }

    private FootsiteProduct parseProduct() {
        try {
            final Request request = new Request.Builder()
                    .url(String.format("https://www.%s/api/products/pdp/%s?timestamp=%s", site.getUrl(), getSku(),
                            new Date().getTime()))
                    .header("accept","application/json")
                    .header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36")
                    .header("x-fl-request-id", UUID.randomUUID().toString())
                    .header("sec-fetch-site", "same-origin")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-dest", "empty")
                    .header("referer","https://www." + site.getSite() + "/")
                    .header("accept-encoding", "deflate, br")
                    .header("accept-language", "en-US,en;q=0.9")
                    .build();

            try(Response response = getHttpClient().newCall(request).execute()) {
                if(response.code() != 200) {
                    return parseProduct();
                }
                final JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();

                if(jsonObject.has("errorList")) {
                    getCallback().onFailure("Product not loaded on PDP endpoint");
                    return null;
                }

                final String image = String.format("https://images.%s/pi/%s/zoom/%s.jpeg", site.getUrl(), getSku(), getSku());
                final String name = jsonObject.get("name").getAsString();
                final JsonArray sellableUnits = jsonObject.getAsJsonArray("sellableUnits");

                final FootsiteProduct footsiteProduct = new FootsiteProduct(name,
                        getSku(),
                        image,
                        String.format("https://www.%s/product/~/%s.html", site.getUrl(),
                                getSku()), site.getSite(), 0);

                for(JsonElement jsonElement : sellableUnits) {
                    if(jsonElement instanceof JsonObject) {
                        final JsonObject sellableUnit = jsonElement.getAsJsonObject();

                        if(footsiteProduct.getPrice() == 0) {
                            footsiteProduct.setPrice(sellableUnit.getAsJsonObject("price").get("value").getAsDouble());
                        }

                        if(sellableUnit.get("stockLevelStatus")
                                .getAsString().equalsIgnoreCase("inStock")) {
                            for(JsonElement attributesElement : sellableUnit.getAsJsonArray("attributes")) {
                                if(attributesElement instanceof JsonObject) {
                                    final JsonObject attributeObject = attributesElement.getAsJsonObject();

                                    if(attributeObject.get("type").getAsString().equalsIgnoreCase("size"))
                                        footsiteProduct.getSizesInStock().add(attributeObject.get("value").getAsString());
                                }
                            }
                        }
                    }
                }

                return footsiteProduct;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isProductLoaded() {
        try {
            final Request request = new Request.Builder()
                    .url(String.format("https://www.%s/product/~/%s.html", site.getUrl(), getSku()))
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36")
                    .build();

            try(Response response = getHttpClient().newCall(request).execute()) {
                final String title = response.body().string().split("<title>")[1].split("</title>")[0];

                return !title.contains(".html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
