package com.fractalmines.web.game.handlers.profile;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.User;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebContext(path = "getGJUserInfo", alternatePaths = {"getGJUserInfo20"})
public class GetProfileHandler extends RequestHandler {

    public GetProfileHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        if (!isExpectedMethod(exchange)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String postArgsRaw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> args = parseForm(postArgsRaw);

        long accountID = Integer.parseInt(args.get("accountID"));
        long targetAccountID = Integer.parseInt(args.get("targetAccountID"));

        User user = Database.getActiveProvider().getUserProvider().getUser(targetAccountID);
        sendResponse(exchange, user.toUserDataString(accountID == targetAccountID));
    }
}
