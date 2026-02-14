package com.fractalmines.web.game.handlers.profile;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.User;
import com.fractalmines.database.providers.DatabaseProvider;
import com.fractalmines.util.UserUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebContext(path = "updateGJUserScore", alternatePaths = { "updateGJUserScore19", "updateGJUserScore20", "updateGJUserScore21", "updateGJUserScore22" })
public class UpdateUserScoreHandler extends RequestHandler {
    public UpdateUserScoreHandler(WebContext context) {
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
        //System.out.println(raw);

        String username = args.get("userName");
        String secret = args.get("secret");
        String gameVersion = args.get("gameVersion");
        String udid = args.get("udid");
        String gjp2 = args.get("gjp2");
        int binaryVersion = Integer.parseInt(args.get("binaryVersion"));
        long accountID = Long.parseLong(args.getOrDefault("accountID", "-1"));
        long uuid = Long.parseLong(args.get("uuid"));

        if (udid == null && accountID == -1L) {
            sendResponse(exchange, "-1");
            return;
        }

        if (!UserUtil.checkAuthenticatedById(accountID, gjp2)) {
            sendResponse(exchange, "-1");
            return;
        }

        DatabaseProvider active = Database.getActiveProvider();
        //Account account = active.getAccountProvider().getAccount(accountID);
        User user = active.getUserProvider().getUser(accountID);

        int stars = Integer.parseInt(args.get("stars"));
        int moons = Integer.parseInt(args.get("moons"));
        int coins = Integer.parseInt(args.get("coins"));
        int userCoins = Integer.parseInt(args.get("userCoins"));
        int diamonds = Integer.parseInt(args.get("diamonds"));
        int demons = Integer.parseInt(args.get("demons"));

        int primaryColor = Integer.parseInt(args.get("color1"));
        int secondaryColor = Integer.parseInt(args.get("color2"));
        int glowColor = Integer.parseInt(args.getOrDefault("color3", "0"));

        int special = args.containsKey("special") ? Integer.parseInt(args.get("special")) : 0;

        int cube = args.containsKey("accIcon") ? Integer.parseInt(args.get("accIcon")) : 0;
        int ship = args.containsKey("accShip") ? Integer.parseInt(args.get("accShip")) : 0;
        int ball = args.containsKey("accBall") ? Integer.parseInt(args.get("accBall")) : 0;
        int ufo = args.containsKey("accBird") ? Integer.parseInt(args.get("accBird")) : 0;
        int wave = args.containsKey("accDart") ? Integer.parseInt(args.get("accDart")) : 0;
        int robot = args.containsKey("accRobot") ? Integer.parseInt(args.get("accRobot")) : 0;
        int spider = args.containsKey("accSpider") ? Integer.parseInt(args.get("accSpider")) : 0;
        int swing = args.containsKey("accSwing") ? Integer.parseInt(args.get("accSwing")) : 0;
        int jetpack = args.containsKey("accJetpack") ? Integer.parseInt(args.get("accJetpack")) : 0;
        int glow = args.containsKey("accGlow") ? Integer.parseInt(args.get("accGlow")) : 0;
        int explosion = args.containsKey("accExplosion") ? Integer.parseInt(args.get("accExplosion")) : 0;

        String sinfo = args.getOrDefault("sinfo", "0,0,0,0,0,0,0,0,0,0,0,0");

        String[] sinfoParts = sinfo.split(",");
        String levelData = String.join(",", sinfoParts[0], sinfoParts[1], sinfoParts[2], sinfoParts[3], sinfoParts[4], sinfoParts[5], sinfoParts[6]);
        String platformerData = String.join(",", sinfoParts[7], sinfoParts[8], sinfoParts[9], sinfoParts[10], sinfoParts[11]);

        System.out.println(levelData);
        System.out.println(platformerData);

        int sInfoD = args.containsKey("sinfod") ? Integer.parseInt(args.get("sinfod")) : 0;
        int sInfoG = args.containsKey("sinfog") ? Integer.parseInt(args.get("sinfog")) : 0;
        int sInfoE = args.containsKey("sinfoe") ? Integer.parseInt(args.get("sinfoe")) : 0;

        String seed = args.get("seed");
        String seed2 = args.get("seed2");

        user.setStars(stars);
        user.setMoons(moons);
        user.setCoins(coins);
        user.setUserCoins(userCoins);
        user.setDiamonds(diamonds);
        user.setDemons(demons);

        user.setCube(cube);
        user.setShip(ship);
        user.setBall(ball);
        user.setUfo(ufo);
        user.setWave(wave);
        user.setRobot(robot);
        user.setSpider(spider);
        user.setSwing(swing);
        user.setJetpack(jetpack);
        user.setGlow(glow);
        user.setExplosion(explosion);

        user.setPrimaryColor(primaryColor);
        user.setSecondaryColor(secondaryColor);
        user.setGlowColor(glowColor);

        // TODO: User demon and star data
        user.setStarInfo(levelData);
        user.setPlatformerInfo(platformerData);

        active.getUserProvider().updateUser(user);

        sendResponse(exchange, String.valueOf(accountID));
    }
}
