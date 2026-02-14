package com.fractalmines.database.providers;

import com.fractalmines.config.DatabaseConfig;
import com.fractalmines.database.providers.mongo.MongoAccountProvider;
import com.fractalmines.database.providers.mongo.MongoCommentProvider;
import com.fractalmines.database.providers.mongo.MongoSaveDataProvider;
import com.fractalmines.database.providers.mongo.MongoUserProvider;
import com.fractalmines.database.providers.sub.AccountProvider;
import com.fractalmines.database.providers.sub.CommentProvider;
import com.fractalmines.database.providers.sub.SaveDataProvider;
import com.fractalmines.database.providers.sub.UserProvider;
import com.fractalmines.util.BsonUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import org.bson.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.stream.Stream;

@Getter
/*
 * We can store the save data in-db for mongo cause its fast.
 */
public class MongoProvider implements DatabaseProvider {

    private MongoClient client;
    private MongoDatabase database;


    private MongoAccountProvider accountProvider;
    private MongoUserProvider userProvider;
    private MongoSaveDataProvider saveDataProvider;
    private CommentProvider commentProvider;


    @Override
    public void connect() {
        System.out.println("DATABASE: Connecting to MongoDB...");

        CodecRegistry registry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(CodecRegistries.fromProviders(MongoClientSettings.getDefaultCodecRegistry(), registry)).applyConnectionString(new ConnectionString(DatabaseConfig.getInstance().getMongo().getConnectionString())).build();
        client = MongoClients.create(settings);
        database = client.getDatabase(DatabaseConfig.getInstance().getMongo().getDatabase());

        initProviders();
        createCollections();
    }

    private void initProviders() {
        accountProvider = new MongoAccountProvider(this);
        userProvider = new MongoUserProvider(this);
        saveDataProvider = new MongoSaveDataProvider(this);
        commentProvider = new MongoCommentProvider(this);
    }

    private void createCollections() {
        System.out.println("DATABASE: Creating MongoDB collections");
        Stream.of(
                "users",
                "accounts",
                "levels",
                "comments",
                "account_comments",
                "indexes",
                "save_data"
        ).forEach(database::createCollection);
    }

    @Override
    public void close() {
        if (client != null) client.close();
    }

    @Override
    public AccountProvider getAccountProvider() {
        return accountProvider == null ? accountProvider = new MongoAccountProvider(this) : accountProvider;
    }
    @Override
    public UserProvider getUserProvider() {
        return userProvider == null ? userProvider = new MongoUserProvider(this) : userProvider;
    }

    @Override
    public SaveDataProvider getSaveDataProvider() {
        return saveDataProvider == null ? saveDataProvider = new MongoSaveDataProvider(this) : saveDataProvider;
    }

    @Override
    public CommentProvider getCommentProvider() {
        return commentProvider == null ? commentProvider = new MongoCommentProvider(this) : commentProvider;
    }

    public long getNextIndex(String name) {
        MongoCollection<Document> collection = database.getCollection("indexes");

        Document updated = collection.findOneAndUpdate(
                Filters.eq("name", name),
                Updates.inc("index", 1L),
                new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER)
        );

        return updated.getLong("index");
    }
}
