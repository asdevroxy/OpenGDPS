package com.fractalmines.database.providers.sub;

import com.fractalmines.database.mongo.Account;

public interface AccountProvider {
    long getAccountId(String username);

    Account getAccount(String username);
    Account getAccountFromEmail(String email);
    Account getAccount(long accountId);

    void createAccount(Account account);
    void updateAccount(Account account);

    boolean idExists(Account account);
    boolean usernameExists(Account account);
    boolean emailExists(Account account);
}
