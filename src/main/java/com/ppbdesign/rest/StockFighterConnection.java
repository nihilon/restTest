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

    private final String apiURL, venue;

    public StockFighterConnection(String apiURL, String venue) {
        logger.setLevel(Level.TRACE);
        logger.info("BEGIN: Connection check");
        logger.info("Set log level: TRACE");

        this.apiURL = apiURL;
        logger.debug("apiURL set to " + apiURL);

        this.venue = venue;
        logger.debug("Venue set: " + venue);

        httpClient = HttpClients.createDefault();
        logger.debug("Default httpClient set");
    }

    public Boolean apiCheck() {
        URIBuilder uriBuilder = new URIBuilder();
        URI apiURI = null;

        try {
            apiURI = uriBuilder.setScheme(scheme)
                               .setHost(host)
                               .setPath("/ob/api/heartbeat")
                               .build();
            logger.debug(apiURI.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet(apiURL);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return (response != null &&
                response.getStatusLine().getStatusCode() == 200);
    }

    public Boolean venueCheck() {
        URIBuilder uriBuilder = new URIBuilder();
        URI venueURI = null;

        try {
            venueURI = uriBuilder.setScheme(scheme)
                                 .setHost(host)
                                 .setPath("/ob/api/venues/" +
                                          venue + "/heartbeat")
                                 .build();
            logger.debug(venueURI.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet(venueURI);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return (response != null &&
                response.getStatusLine().getStatusCode() == 200);
    }

    private JsonObject convertResponseToJsonObject(HttpResponse httpResponse) {
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
}
