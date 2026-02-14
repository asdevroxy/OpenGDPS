package com.fractalmines.web.game.handlers.account.savedata;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.SaveData;
import com.fractalmines.database.mongo.User;
import com.fractalmines.util.SaveDataUtil;
import com.fractalmines.util.UserUtil;
import com.fractalmines.web.RequestHandler;
import com.fractalmines.web.WebContext;
import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

@WebContext(path = "accounts/backupGJAccount", alternatePaths = { "database/accounts/backupGJAccountNew" })
public class UploadSaveDataHandler extends RequestHandler {
    public UploadSaveDataHandler(WebContext context) {
        super(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws Exception {
        System.out.println("b ");

        if (!isExpectedMethod(exchange)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.ISO_8859_1);
        Map<String, String> args = parseForm(raw);
        System.out.println(raw);

        int gameVersion = Integer.parseInt(args.get("gameVersion"));
        int binaryVersion = Integer.parseInt(args.get("binaryVersion"));
        String udid = args.get("udid");
        long uuid = Long.parseLong(args.get("uuid"));
        long accountID = Long.parseLong(args.get("accountID"));
        String gjp2 = args.containsKey("password") ? args.get("password") : args.get("gjp2");
        String saveDataRaw = args.get("saveData");

        if (!UserUtil.checkAuthenticatedById(accountID, gjp2)) {
            exchange.sendResponseHeaders(403, -1);
            return;
        }

        String[] parts = saveDataRaw.split(";");
        String ccGameData = parts[0].replace("-", "+").replace("_", "/");
        String decodedBase64 = new String(Base64.getDecoder().decode(ccGameData), StandardCharsets.ISO_8859_1);
        String decodedSaveData = decodeSaveData(decodedBase64);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dataXml = builder.parse(new ByteArrayInputStream(decodedSaveData.getBytes(StandardCharsets.UTF_8)));

        int orbs = SaveDataUtil.extractOrbs(dataXml);
        int completedlevels = SaveDataUtil.extractCompletedLevels(dataXml);

//
        User user = Database.getActiveProvider().getUserProvider().getUser(accountID);

        user.setOrbs(orbs);
        user.setCompletedLevels(completedlevels);

        Database.getActiveProvider().getUserProvider().updateUser(user);
//

        SaveData saveData = new SaveData();
        saveData.setAccountID(accountID);
        saveData.setData(saveDataRaw); // new versions of gd don't store the password in the file. we aren't targeting old gd, so we don't need to remove it.

        Database.getActiveProvider().getSaveDataProvider().storeSaveData(accountID, saveData);

        sendResponse(exchange, "1");
    }

    private static String decodeSaveData(String decoded) throws IOException {
        String decodedSaveData;
        byte[] buffer = new byte[1024];
        int len;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(decoded.getBytes(StandardCharsets.ISO_8859_1));
             GZIPInputStream gis = new GZIPInputStream(bais);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            while ((len = gis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            decodedSaveData = out.toString(StandardCharsets.UTF_8); // now safe to convert
        }
        return decodedSaveData;
    }

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // & 0xFF ensures the byte is treated as unsigned
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    private String gzlibDecode(String in) {
        Inflater inflater = new Inflater(true);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8));
             InflaterInputStream iis = new InflaterInputStream(bais, inflater);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = iis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toString(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
