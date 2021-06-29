package com.glitchsoftware.monitor.proxy;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Brennan
 * @since 6/28/21
 **/
public class ProxyManager {
    private final List<CustomProxy> proxies = new LinkedList<>();
    private final Random random = new Random();

    public ProxyManager() {

    }

    public CustomProxy randomProxy() {
        final CustomProxy customProxy = proxies.get(random.nextInt(proxies.size()));
        proxies.remove(customProxy);

        return customProxy;
    }
}
