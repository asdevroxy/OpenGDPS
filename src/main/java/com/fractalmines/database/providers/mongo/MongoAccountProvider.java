package com.fractalmines.database.providers.mongo;

import com.fractalmines.database.mongo.Account;
import com.fractalmines.database.providers.MongoProvider;
import com.fractalmines.database.providers.sub.AccountProvider;
import lombok.RequiredArgsConstructor;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;

@RequiredArgsConstructor
public class MongoAccountProvider implements AccountProvider {
    private final MongoProvider provider;

    @Override
    public long getAccountId(String username) {
        Account account = getAccount(username);
        if (account == null) return -1;

        return account.getAccountID();
    }

    @Override
    public Account getAccount(String username) {
        BsonDocument searchQuery = new BsonDocument();
        searchQuery.append("username", new BsonString(username));

        return provider.getDatabase().getCollection("accounts", Account.class).find(searchQuery).first();
    }

    @Override
    public Account getAccountFromEmail(String email) {
        BsonDocument searchQuery = new BsonDocument();
        searchQuery.append("email", new BsonString(email));

        return provider.getDatabase().getCollection("accounts", Account.class).find(searchQuery).first();
    }

    @Override
    public Account getAccount(long accountId) {
        BsonDocument searchQuery = new BsonDocument();
        searchQuery.append("accountID", new BsonInt64(accountId));

        return provider.getDatabase().getCollection("accounts", Account.class).find(searchQuery).first();
    }

    @Override
    public void createAccount(Account account) {
        account.setAccountID(provider.getNextIndex("accounts"));
        provider.getDatabase().getCollection("accounts", Account.class).insertOne(account);
    }

    @Override
    public void updateAccount(Account account) {
        BsonDocument searchQuery = new BsonDocument();
        searchQuery.append("accountID", new BsonInt64(account.getAccountID()));

        provider.getDatabase().getCollection("accounts", Account.class).replaceOne(searchQuery, account);
    }

    @Override
    public boolean idExists(Account account) {
        return account.getAccountID() != -1 && getAccount(account.getAccountID()) != null;
    }

    @Override
    public boolean usernameExists(Account account) {
        return getAccount(account.getUsername()) != null;
    }

    @Override
    public boolean emailExists(Account account) {
        return getAccountFromEmail(account.getEmail()) != null;
    }
}
