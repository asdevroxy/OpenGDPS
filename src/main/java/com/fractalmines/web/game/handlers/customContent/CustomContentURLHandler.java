package com.fractalmines.web.game.handlers.customContent;

import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

@WebContext(path = "getCustomContentURL")
public class CustomContentURLHandler extends RequestHandler {
    public CustomContentURLHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        sendResponse(exchange, "https://geometrydashfiles.b-cdn.net");
    }
}
