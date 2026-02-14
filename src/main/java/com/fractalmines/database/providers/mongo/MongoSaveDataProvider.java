package com.fractalmines.database.providers.mongo;

import com.fractalmines.database.mongo.SaveData;
import com.fractalmines.database.providers.MongoProvider;
import com.fractalmines.database.providers.sub.SaveDataProvider;
import com.fractalmines.util.BsonUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.lang.Nullable;
import org.bson.BsonInt64;

public class MongoSaveDataProvider implements SaveDataProvider {
    private final MongoProvider provider;
    private MongoCollection<SaveData> collection;

    public MongoSaveDataProvider(MongoProvider provider) {
        this.provider = provider;
        this.collection = provider.getDatabase().getCollection("save_data", SaveData.class);
    }

    @Override
    public @Nullable SaveData loadSaveData(long accountID) {
        return collection.find(BsonUtil.singleKeyValue("accountID", new BsonInt64(accountID))).first();
    }

    @Override
    public void storeSaveData(long accountID, SaveData saveData) {
        if (loadSaveData(accountID) != null) {
            collection.replaceOne(BsonUtil.singleKeyValue("accountID", new BsonInt64(accountID)), saveData);
            return;
        }

        collection.insertOne(saveData);
    }
}
