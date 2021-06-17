package com.glitchsoftware.monitor.socket;

import com.glitchsoftware.monitor.Monitor;
import com.glitchsoftware.monitor.socket.channel.Channel;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

/**
 * @author Brennan
 * @since 6/17/2021
 **/
public class WebSocketHandler extends BaseWebSocketHandler {

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        super.onOpen(connection);
        Monitor.INSTANCE.addChannel(new Channel(connection));
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        super.onClose(connection);
        Monitor.INSTANCE.removeChannel(Monitor.INSTANCE.getChannelByConnection(connection));
    }

    @Override
    public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
        super.onMessage(connection, msg);
    }
}
