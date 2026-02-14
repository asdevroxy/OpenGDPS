package com.fractalmines.util;

import com.fractalmines.database.Database;
import com.fractalmines.database.mongo.Account;
import com.fractalmines.database.providers.DatabaseProvider;
import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

@UtilityClass
public class UserUtil {
    public boolean checkAuthenticated(String username, String password) {
        return checkAuthenticatedById(getAccountId(username), password);
    }

    public boolean checkAuthenticatedLegacy(String username, String password) {
        DatabaseProvider provider = Database.getActiveProvider();
        return checkAuthenticatedByIdLegacy(provider.getAccountProvider().getAccountId(username), password);
    }

    public boolean checkAuthenticatedById(long accountId, String password) {
        Account account = Database.getActiveProvider().getAccountProvider().getAccount(accountId);
        if (account == null) return false;

        return BCrypt.checkpw(password, account.getGjp2());
    }

    public boolean checkAuthenticatedByIdLegacy(long accountId, String password) {
        return false;
    }

    public String attempt(String username, String password) {
        return attemptById(getAccountId(username), password);
    }

    public String attemptById(long accountId, String password) {
        return "-12";
    }

    public long getAccountId(String username) {
        return Database.getActiveProvider().getAccountProvider().getAccountId(username);
    }

}
