/* ================================================================
 * Author: Joel Chavez
 * Date: 12/28/2015
 * Time: 10:46 AM
 * Purpose: <Enter a project/file description here>
 * Created with IntelliJ IDEA
 * ================================================================ */
package com.ppbdesign.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.http.client.methods.HttpGet;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class VenuesAndStocks {
    private final Logger logger =
            (Logger) LoggerFactory.getLogger(VenuesAndStocks.class);

    VenuesAndStocks() {
        DateTime sessionTimeStamp = new DateTime();
        logger.setLevel(Level.TRACE);
        logger.info("Set logging level: TRACE");
        logger.info("BEGIN: VenuesAndStocks " + sessionTimeStamp.toString());
    }

    public static Boolean apiCheck() {
        URL url;
        URI uri = null;
        try {
            url = new URL("https://api.stockfighter.io/ob/api/heartbeat");
            uri = url.toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri != null) {
            HttpGet httpGet = new HttpGet(uri);
        } else {
            throw new NullPointerException("URI is null!");
        }
        return null;
    }
}
