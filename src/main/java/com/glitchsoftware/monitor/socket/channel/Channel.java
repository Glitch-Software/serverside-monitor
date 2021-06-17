package com.glitchsoftware.monitor.socket.channel;

import com.glitchsoftware.monitor.socket.notification.Notification;
import org.webbitserver.WebSocketConnection;

/**
 * @author Brennan
 * @since 6/17/2021
 **/
public class Channel {

    private WebSocketConnection connection;

    public Channel(WebSocketConnection connection) {
        this.connection = connection;
    }

    public void send(Notification notification) {
        connection.send(notification.toJSON().toString());
    }

    public WebSocketConnection getConnection() {
        return connection;
    }
}
