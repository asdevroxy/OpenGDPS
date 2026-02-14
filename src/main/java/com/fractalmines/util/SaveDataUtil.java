package com.fractalmines.util;

import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class SaveDataUtil {

    public int extractOrbs(Document saveDataXml) throws Exception {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        String expression = "//k[text()='14']/following-sibling::s[1]";
        String result = xpath.evaluate(expression, saveDataXml);

        return result == null ? 0 : Integer.parseInt(result);
    }

    public int extractCompletedLevels(Document saveDataXml) throws Exception {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        String expression = "//k[text()='GS_value']/following-sibling::d//k[text()='4']/following-sibling::s[1]";
        String result = xpath.evaluate(expression, saveDataXml);

        return result == null ? 0 : Integer.parseInt(result);
    }
}
