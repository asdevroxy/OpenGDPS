package com.fractalmines.web;

import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class RequestHandler {

    @Getter private final WebContext context;
    public RequestHandler(WebContext context) {
        this.context = context;
    }
    public abstract void handle(HttpExchange exchange) throws Exception;

    public boolean isExpectedMethod(HttpExchange exchange) {
        return exchange.getRequestMethod().equalsIgnoreCase(context.method());
    }


    public Map<String, String> parseForm(String body) {
        Map<String, String> result = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2); // split into key and value
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            result.put(key, value);
        }
        return result;
    }

    public void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
    }
}
