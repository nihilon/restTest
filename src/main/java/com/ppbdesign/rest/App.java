package com.ppbdesign.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {
    private static Logger logger =
            (Logger) LoggerFactory.getLogger("com.ppbdesign.rest.App");

    public static void main(String[] args) throws IOException {
        logger.setLevel(Level.TRACE);
        VenuesAndStocks venuesAndStocks = new VenuesAndStocks();
    }
}
