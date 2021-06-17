package com.glitchsoftware.monitor.socket.notification;

import com.google.gson.JsonObject;

/**
 * @author Brennan
 * @since 6/17/2021
 **/
public interface Notification {

    JsonObject toJSON();

}
