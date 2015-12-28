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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

public class StockFighterConnection {
    private final String scheme = "http";
    private final String host   = "api.stockfighter.io";

    private final DateTime instanceLoadTime = new DateTime();
    private final Logger   logger           =
            (Logger) LoggerFactory.getLogger("com.ppbdesign.rest" +
                                             ".StockFighterConnection" +
                                             instanceLoadTime.toString());

    private final CloseableHttpClient httpClient;

    private final String venueCode;

    public StockFighterConnection(String venueCode) {
        logger.setLevel(Level.TRACE);
        logger.info("BEGIN: StockFighterConnection");
        logger.info("Set log level: TRACE");

        this.venueCode = venueCode;
        logger.debug("Venue set: " + venueCode);

        httpClient = HttpClients.createDefault();
        logger.debug("Default httpClient set");

        logger.debug("Performing connection checks...");
        apiCheck();
        venueCheck();
        logger.debug("Connection checks complete");
    }

    private Boolean apiCheck() {
        HttpGet httpGet = new HttpGet(uriBuilder(URIType.API));
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            logger.debug("API Response: " + response.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return (response != null &&
                response.getStatusLine().getStatusCode() == 200);
    }

    private Boolean venueCheck() {
        HttpGet httpGet = new HttpGet(uriBuilder(URIType.VENUE));
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            logger.debug("Venue response " + response.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return (response != null &&
                response.getStatusLine().getStatusCode() == 200);
    }

    private URI uriBuilder(URIType uriType) {
        URIBuilder uriB = new URIBuilder();
        URI uri = null;
        String logMsgFormat = "StockFighterConnection.uriBuilder(%s): ";

        try {
            uri = uriB.setScheme(scheme).setHost(host).build();
        } catch (URISyntaxException USE) {
            USE.printStackTrace();
        }

        switch (uriType) {
            case VENUE: // Venue URI
                try {
                    uri = uriB.setPath(PathPrefix.VENUE +
                                       venueCode + "/" +
                                       PathPostfix.HEARTBEAT)
                              .build();
                    logger.debug(String.format(logMsgFormat, URIType.VENUE) +
                                 uri.toString());
                } catch (URISyntaxException USE) {
                    USE.printStackTrace();
                }
                return uri;
            case API: // API top-level URI
                try {
                    uri = uriB.setPath(PathPrefix.API +
                                       PathPostfix.HEARTBEAT)
                              .build();
                    logger.debug(String.format(logMsgFormat, URIType.API) +
                                 uri.toString());
                } catch (URISyntaxException USE) {
                    USE.printStackTrace();
                }
                return uri;
            default:
                // Defaults to API heartbeat URI
                try {
                    uri = uriB.setPath(PathPrefix.API +
                                       PathPostfix.HEARTBEAT)
                              .build();
                    logger.debug(String.format(logMsgFormat, URIType.API) +
                                 uri.toString());
                } catch (URISyntaxException USE) {
                    USE.printStackTrace();
                }
                return uri;
        }
    }

    private JsonObject convertResponseContentToJson(HttpResponse httpResponse) {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(httpResponse.getEntity().getContent(),
                         writer);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        JsonElement jsonElement = new JsonParser().parse(writer.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        logger.debug("Response content: " + jsonObject.toString());

        return jsonObject;
    }

    private enum URIType {
        VENUE,
        API
    }

    private class PathPrefix {
        public static final String API   = "/ob/api/";
        public static final String VENUE = "/ob/api/venues/";
    }

    private class PathPostfix {
        public static final String HEARTBEAT = "heartbeat";
    }
}
