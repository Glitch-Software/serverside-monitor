package com.glitchsoftware.monitor.task.impl.footsites;

/**
 * @author Brennan
 * @since 6/27/21
 **/
public enum Footsites {
    FOOTLOCKER_US("Footlocker US", "footlocker.com"),
    FOOTLOCKER_CA("Footlocker CA", "footlocker.ca"),
    KIDS_FOOTLOCKER("Kids Footlocker", "kidsfootlocker.com"),
    FOOTACTION("Footaction", "footaction.com"),
    CHAMPSSPORTS("Champs Sports", "champssports.com"),
    EASTBAY("Eastbay", "eastbay.com");

    private final String site, url;

    Footsites(String site, String url) {
        this.site = site;
        this.url = url;
    }

    public String getSite() {
        return site;
    }

    public String getUrl() {
        return url;
    }
}
