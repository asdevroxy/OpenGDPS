package com.fractalmines.web.game.handlers.account;

import com.fractalmines.config.Config;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

@WebContext(path = "getAccountURL")
public class GetAccountUrlHandler extends RequestHandler {
    public GetAccountUrlHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        if (!isExpectedMethod(exchange)) {
            System.out.println("err");
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        System.out.println(Config.getInstance().getWebSecurity().getAccountsUrl());

        sendResponse(exchange, Config.getInstance().getWebSecurity().getAccountsUrl());
    }
}
