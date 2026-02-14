package com.fractalmines.web.game.handlers.profile;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.Comment;
import com.fractalmines.util.GDUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@WebContext(path = "getGJAccountComments", alternatePaths = { "getGJAccountComments19", "getGJAccountComments20" })
public class GetAccountCommentsHandler extends RequestHandler {
    public GetAccountCommentsHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        if (!isExpectedMethod(exchange)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(raw);

        Map<String, String> args = parseForm(raw);

        int gameVersion = Integer.parseInt(args.get("gameVersion"));
        int binaryVersion = Integer.parseInt(args.get("binaryVersion"));
        long accountID = Long.parseLong(args.get("accountID")); // fsr the game send this argument twice? the fuck robtop?
        String gjp2 = args.get("gjp2");
        String secret = args.get("secret");

        int page = Integer.parseInt(args.get("page"));
        int total = Integer.parseInt(args.get("total"));

        List<Comment> comments = Database.getActiveProvider().getCommentProvider().getAccountComments(accountID, page);
        if (comments.isEmpty()) {
            sendResponse(exchange, "#0:0:0");
            return;
        }

        StringBuilder builder = new StringBuilder();

        comments.forEach(comment -> {
            builder.append("|").append("2~").append(comment.getComment()).append("~3~").append(comment.getAccountID()).append("~4~").append(comment.getLikes())
                    .append("~5~0~7~").append(comment.isSpam() ? 1 : 0).append("~9~").append(GDUtil.timeAgo(Instant.ofEpochMilli(comment.getPostDate())))
                    .append("~6~").append(comment.getId());
        });

        builder.append("#").append(Database.getActiveProvider().getCommentProvider().getTotalAccountComments(accountID)).append(":").append(page * 10).append(":10");

        sendResponse(exchange, builder.substring(1));
    }
}
