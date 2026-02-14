package com.fractalmines.database.providers.mongo;

import com.fractalmines.database.mongo.Account;
import com.fractalmines.database.mongo.User;
import com.fractalmines.database.providers.MongoProvider;
import com.fractalmines.database.providers.sub.UserProvider;
import com.fractalmines.util.BsonUtil;
import com.mongodb.client.MongoCollection;
import org.bson.BsonDocument;
import org.bson.BsonInt64;

public class MongoUserProvider implements UserProvider {
    private final MongoProvider provider;
    private MongoCollection<User> usersCollection;

    public MongoUserProvider(MongoProvider provider) {
        this.provider = provider;

        usersCollection = provider.getDatabase().getCollection("users", User.class);
    }

    @Override
    public User getUser(long accountID) {
        BsonDocument searchQuery = BsonUtil.singleKeyValue("accountID", new BsonInt64(accountID));
        return usersCollection.find(searchQuery).first();
    }

    @Override
    public void createUser(Account account, User user) {
        if (getUser(account.getAccountID()) != null) {
            updateUser(user);
            return;
        }

        user.setId(account.getAccountID());
        user.setAccountID(account.getAccountID());
        usersCollection.insertOne(user);
    }

    @Override
    public void updateUser(User user) {
        BsonDocument searchQuery = BsonUtil.singleKeyValue("accountID", new BsonInt64(user.getAccountID()));
        usersCollection.replaceOne(searchQuery, user);
    }

    @Override
    public void deleteUser(User user) {
        usersCollection.findOneAndDelete(BsonUtil.singleKeyValue("accountID", new BsonInt64(user.getAccountID())));
    }
}
