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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class HttpStatusCheck {
    private final DateTime instanceLoadTime = new DateTime();
    private final Logger   logger           =
            (Logger) LoggerFactory.getLogger("com.ppbdesign.rest" +
                                             ".ConnectionCheck" +
                                             instanceLoadTime.toString());

    private final CloseableHttpClient   httpClient;
    private final HttpGet               httpGet;
    private final CloseableHttpResponse response;

    public HttpStatusCheck(String connectionURL) {
        logger.setLevel(Level.TRACE);
        logger.info("BEGIN: CheckConnection");
        logger.info("Set CheckConnection log level: TRACE");

        logger.debug("connectionURL set to " + connectionURL);

        httpClient = HttpClients.createDefault();
        logger.debug("Default httpClient set");

        httpGet = new HttpGet(connectionURL);
        logger.info("HTTP GET called on " + connectionURL + ". Attempting to " +
                    "get response...");

        response = getResponse();
    }

    private CloseableHttpResponse getResponse() {
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response == null) {
                throw new NullPointerException("Response is null!");
            } else {
                logger.debug("Success");
                logger.debug("Response: " + response.toString());
                logger.debug("Response status line: " +
                             response.getStatusLine());
            }

            httpClient.close();
            logger.debug("HttpClient closed");
        } catch (IOException IOE) {
            logger.error(Arrays.toString(IOE.getStackTrace()));
        } catch (NullPointerException NPE) {
            logger.error(Arrays.toString(NPE.getStackTrace()));
        }
        return response;
    }
}
