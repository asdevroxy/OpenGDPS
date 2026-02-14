package com.fractalmines.web.game.handlers.account;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.Account;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebContext(path = "accounts/loginGJAccount")
public class AccountLoginHandler extends RequestHandler {
    public AccountLoginHandler(WebContext context) {
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
        System.out.println(postArgsRaw);

        Account account = Database.getActiveProvider().getAccountProvider().getAccount(args.get("userName"));
        if (account == null) {
            sendResponse(exchange, "-1");
            return;
        }

        String password = args.getOrDefault("gjp2", args.get("password"));
        boolean valid = BCrypt.checkpw(password, account.getGjp2());

        if (!valid) {
            sendResponse(exchange, "-1");
            return;
        }

        if (!account.isActive()) {
            sendResponse(exchange, "-2");
            return;
        }

        sendResponse(exchange, "%d,%d".formatted(account.getAccountID(), account.getAccountID()));
    }
}
