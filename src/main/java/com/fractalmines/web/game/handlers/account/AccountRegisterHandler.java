package com.fractalmines.web.game.handlers.account;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.Account;
import com.fractalmines.database.mongo.User;
import com.fractalmines.database.providers.sub.AccountProvider;
import com.fractalmines.util.CipherUtil;
import com.fractalmines.util.GDUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebContext(path = "accounts/registerGJAccount")
public class AccountRegisterHandler extends RequestHandler {
    public AccountRegisterHandler(WebContext context) {
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

        if (GDUtil.isMissingAnyKeys(args, "userName", "email", "password", "secret")) {
            sendResponse(exchange, "-1");
            return;
        }

        if (args.get("userName").length() > 20) {
            sendResponse(exchange, "-4");
            return;
        }

        Account account = new Account();
        account.setUsername(args.get("userName").toLowerCase());
        account.setEmail(args.get("email"));
        account.setLegacyPassword(BCrypt.hashpw(CipherUtil.sha1Hash(args.get("password"), CipherUtil.SHA1_PASSWORD), BCrypt.gensalt()));
        account.setGjp2(account.getLegacyPassword());
        account.setSecret(args.get("secret"));
        account.setActive(true);

        User user = new User();
        user.setUsername(account.getUsername());
        user.setId(account.getAccountID());
        user.setAccountID(account.getAccountID());

        AccountProvider accountProvider = Database.getActiveProvider().getAccountProvider();
        if (accountProvider.usernameExists(account)) {
            sendResponse(exchange, "-2");
            return;
        }
        if (accountProvider.emailExists(account)) {
            sendResponse(exchange, "-3");
            return;
        }

        if (accountProvider.idExists(account)) {
            sendResponse(exchange, "-1");
            return;
        }

        Database.getActiveProvider().getAccountProvider().createAccount(account);
        Database.getActiveProvider().getUserProvider().createUser(account, user);

        sendResponse(exchange, "1");
    }
}
