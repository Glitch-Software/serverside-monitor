package com.glitchsoftware.monitor.task;

import com.glitchsoftware.monitor.task.callback.MonitorCallback;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
@Getter
@Setter
public abstract class Task implements Runnable {

    private final MonitorCallback callback;
    private OkHttpClient httpClient = new OkHttpClient.Builder().build();

    private boolean started;
    private String sku;

    public Task(MonitorCallback callback, String sku) {
        this.callback = callback;
        this.sku = sku;

        rotateProxy();
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void rotateProxy() {
//        final CustomProxy customProxy = AceMonitors.getINSTANCE().getProxyManager().randomProxy();
//
        final Authenticator proxyAuthenticator = (route, response) -> {
            final String credential = Credentials.basic("StarBeam", "ScrimFoots");

            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        };

        setHttpClient(new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("104.255.175.236", 32743)))
                .proxyAuthenticator(proxyAuthenticator)
                .build());
    }
}
