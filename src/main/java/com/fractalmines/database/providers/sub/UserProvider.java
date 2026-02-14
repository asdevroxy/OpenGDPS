package com.fractalmines.database.providers.sub;

import com.fractalmines.database.mongo.Account;
import com.fractalmines.database.mongo.User;

public interface UserProvider {
    User getUser(long accountID);

    void createUser(Account account, User user);
    void updateUser(User user);

    void deleteUser(User user);
}
