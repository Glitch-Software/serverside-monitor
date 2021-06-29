package com.glitchsoftware.monitor.proxy;

/**
 * @author Brennan
 * @since 6/28/21
 **/
public class CustomProxy {
    private String ip, username, password;
    private int port;

    public CustomProxy(String ip, String username, String password, int port) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }
}
