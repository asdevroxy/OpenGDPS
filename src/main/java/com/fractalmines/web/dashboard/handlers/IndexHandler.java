package com.fractalmines.web.dashboard.handlers;

import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;

@WebContext(path = "dashboard")
public class IndexHandler extends RequestHandler {
    private String indexData;
    public IndexHandler(WebContext context) {
        super(context);


    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        if (indexData == null) {
            indexData = new String(getClass().getClassLoader().getResourceAsStream("dashboard/index.html").readAllBytes(), StandardCharsets.UTF_8);
        }

        exchange.sendResponseHeaders(200, indexData.length());
        exchange.getResponseBody().write(indexData.getBytes(StandardCharsets.UTF_8));
    }
}
