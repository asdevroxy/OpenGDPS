package com.fractalmines.web.game.handlers.profile;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.Comment;
import com.fractalmines.database.mongo.SaveData;
import com.fractalmines.util.GDUtil;
import com.fractalmines.util.UserUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.mongodb.internal.connection.DescriptionHelper;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@WebContext(path = "uploadGJAccComment", alternatePaths = { "uploadGJAccComment20" })
public class PostAccountCommentHandler extends RequestHandler {
    public PostAccountCommentHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        if (!isExpectedMethod(exchange)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> args = parseForm(raw);

        if (GDUtil.isMissingAnyKeys(args, "gameVersion", "binaryVersion", "udid", "accountID", "gjp2", "userName", "comment", "secret", "cType", "chk")) {
            sendResponse(exchange, "-1");
            return;
        }

        int gameVersion = Integer.parseInt(args.get("gameVersion"));
        int binaryVersion = Integer.parseInt(args.get("binaryVersion"));
        long accountID = Long.parseLong(args.get("accountID")); // fsr the game send this argument twice? the fuck robtop?
        long uuid = Long.parseLong(args.get("uuid"));
        String gjp2 = args.get("gjp2"); // why is this needed wut
        String secret = args.get("secret");
        String commentBase64 = args.get("comment");
        String udid = args.get("udid");

        if (!UserUtil.checkAuthenticatedById(accountID, gjp2)) {
            sendResponse(exchange, "-1");
            return;
        }

        String decoded = new String(Base64.getDecoder().decode(commentBase64), StandardCharsets.UTF_8);
        if (decoded.isBlank() || decoded.length() > 140) {
            sendResponse(exchange, "-1");
            return;
        }

        Comment comment = new Comment();
        comment.setAccountID(accountID);
        comment.setComment(commentBase64);
        comment.setPostDate(Instant.now().toEpochMilli());

        Database.getActiveProvider().getCommentProvider().addAccountComment(accountID, comment);
        sendResponse(exchange, "1");
    }
}
