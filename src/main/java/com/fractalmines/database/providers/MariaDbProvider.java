package com.fractalmines.database.providers;

import com.fractalmines.database.providers.sub.AccountProvider;
import com.fractalmines.database.providers.sub.CommentProvider;
import com.fractalmines.database.providers.sub.SaveDataProvider;
import com.fractalmines.database.providers.sub.UserProvider;

public class MariaDbProvider implements DatabaseProvider {
    @Override
    public void connect() {

    }

    @Override
    public void close() {

    }

    @Override
    public AccountProvider getAccountProvider() {
        return null;
    }

    @Override
    public UserProvider getUserProvider() {
        return null;
    }

    @Override
    public SaveDataProvider getSaveDataProvider() {
        return null;
    }

    @Override
    public CommentProvider getCommentProvider() {
        return null;
    }
}
