package com.fractalmines.database.providers;

import com.fractalmines.database.providers.sub.AccountProvider;
import com.fractalmines.database.providers.sub.CommentProvider;
import com.fractalmines.database.providers.sub.SaveDataProvider;
import com.fractalmines.database.providers.sub.UserProvider;

public interface DatabaseProvider {
    void connect();
    void close();

    AccountProvider getAccountProvider();
    UserProvider getUserProvider();

    SaveDataProvider getSaveDataProvider();
    CommentProvider getCommentProvider();
}
