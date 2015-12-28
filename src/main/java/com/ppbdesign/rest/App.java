package com.ppbdesign.rest;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String connectionURL = "https://api.stockfighter" +
                               ".io/ob/api/venues/BRBAEX/stocks";
        StockFighterConnection stockFighterConnection =
                new StockFighterConnection(connectionURL);

    }
}
