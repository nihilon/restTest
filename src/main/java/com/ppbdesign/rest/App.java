package com.ppbdesign.rest;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String connectionURL = "https://api.stockfighter" +
                               ".io/ob/api/venues/EAYDEX/stocks";
        HttpStatusCheck httpStatusCheck =
                new HttpStatusCheck(connectionURL);

    }
}
