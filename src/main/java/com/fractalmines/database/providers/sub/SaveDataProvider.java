package com.fractalmines.database.providers.sub;

import com.fractalmines.database.mongo.Account;
import com.fractalmines.database.mongo.SaveData;

public interface SaveDataProvider {


    SaveData loadSaveData(long accountID);

    void storeSaveData(long accountId, SaveData saveData);
    default void storeSaveData(Account account, SaveData saveData) { storeSaveData(account.getAccountID(), saveData); }

}
