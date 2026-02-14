package com.fractalmines.web.game.handlers.account.savedata;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.SaveData;
import com.fractalmines.util.GDUtil;
import com.fractalmines.util.UserUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebContext(path = "accounts/syncGJAccount", alternatePaths = {"database/accounts/syncGJAccountNew", "accounts/syncGJAccount20"})
public class SyncSaveDataHandler extends RequestHandler {
    public SyncSaveDataHandler(WebContext context) {
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

        if (GDUtil.isMissingAnyKeys(args, "gjp2", "gameVersion", "binaryVersion", "uuid", "accountID", "udid")) {
            exchange.sendResponseHeaders(403, -1);
            return;
        }

        long accountID = Long.parseLong(args.get("accountID"));
        String gjp2 = args.get("gjp2");

        if (!UserUtil.checkAuthenticatedById(accountID, gjp2)) {
            exchange.sendResponseHeaders(403, -1);
            return;
        }

        SaveData data = Database.getActiveProvider().getSaveDataProvider().loadSaveData(accountID);
        if (data == null) {
            sendResponse(exchange, "-1");
            return;
        }

        sendResponse(exchange, data.getData() + ";21;30;a;a");
    }
}
