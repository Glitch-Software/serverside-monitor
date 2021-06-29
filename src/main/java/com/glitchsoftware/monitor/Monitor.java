package com.glitchsoftware.monitor;

import com.glitchsoftware.monitor.proxy.ProxyManager;
import com.glitchsoftware.monitor.socket.WebSocketHandler;
import com.glitchsoftware.monitor.socket.channel.Channel;
import com.glitchsoftware.monitor.socket.notification.Notification;
import com.glitchsoftware.monitor.task.Task;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.handler.StaticFileHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Brennan
 * @since 6/11/2021
 **/
public enum Monitor {
    INSTANCE;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final ProxyManager proxyManager = new ProxyManager();

    private final List<Channel> connectedChannel = new LinkedList<>();

    public void startServer() {
        WebServer webServer = WebServers.createWebServer(4317)
                .add("/ws", new WebSocketHandler())
                .add(new StaticFileHandler("/web"));
        webServer.start();

        System.out.println("Started server");
    }

    public void newTask(Task task) {
        executorService.execute(task);
    }

    public void addChannel(Channel channel) {
        System.out.println("New Channel");
        this.connectedChannel.add(channel);
    }

    public void removeChannel(Channel channel) {
        System.out.println("Removed Channel");
        this.connectedChannel.remove(channel);
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public void sendNotification(Notification notification) {
        System.out.println("Sending notification to " + getConnectedChannel().size() + " channels");
        final Iterator<Channel> channelIterator = getConnectedChannel().iterator();

        while (channelIterator.hasNext())
            channelIterator.next().send(notification);
    }

    public Channel getChannelByConnection(WebSocketConnection connection) {
        for(Channel channel : getConnectedChannel()) {
            if(channel.getConnection() == connection)
                return channel;
        }

        return null;
    }

    public List<Channel> getConnectedChannel() {
        return connectedChannel;
    }
}
