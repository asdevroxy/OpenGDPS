package com.fractalmines.util;

import lombok.experimental.UtilityClass;
import org.bson.BsonDocument;
import org.bson.BsonValue;

@UtilityClass
public class BsonUtil {

    public BsonDocument singleKeyValue(String key, BsonValue value) {
        BsonDocument document = new BsonDocument();
        document.append(key, value);

        return document;
    }

}
