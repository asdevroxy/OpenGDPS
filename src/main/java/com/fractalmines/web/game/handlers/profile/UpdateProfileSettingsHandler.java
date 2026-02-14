package com.fractalmines.web.game.handlers.profile;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.User;
import com.fractalmines.database.providers.sub.UserProvider;
import com.fractalmines.util.GDUtil;
import com.fractalmines.util.UserUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebContext(path = "updateGJAccSettings20", alternatePaths = { "updateGJAccSettings" })
public class UpdateProfileSettingsHandler extends RequestHandler {
    public UpdateProfileSettingsHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        if (!isExpectedMethod(exchange)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String,String> args = parseForm(raw);

        if (GDUtil.isMissingAnyKeys(args, "accountID", "gjp2", "mS", "frS", "cS", "yt", "twitter", "twitch")) {
            sendResponse(exchange, "-1");
            return;
        }

        long accountID = Long.parseLong(args.get("accountID"));
        String gjp2 = args.get("gjp2");
        int messageState = Integer.parseInt(args.get("mS"));
        int friendReqState = Integer.parseInt(args.get("mS"));
        int commentState = Integer.parseInt(args.get("mS"));
        String youtube = args.get("youtube");
        String twitter = args.get("twitter");
        String twitch  = args.get("twitch");

        if (!UserUtil.checkAuthenticatedById(accountID, gjp2)) {
            exchange.sendResponseHeaders(403, -1);
            return;
        }

        UserProvider provider = Database.getActiveProvider().getUserProvider();
        User user = provider.getUser(accountID);

        user.setMessageState(messageState);
        user.setFriendState(friendReqState);
        user.setCommentState(commentState);
        user.setYoutubeUrl(youtube);
        user.setTwitterUrl(twitter);
        user.setTwitchUrl(twitch);

        provider.updateUser(user);
        sendResponse(exchange, "1");
    }
}
