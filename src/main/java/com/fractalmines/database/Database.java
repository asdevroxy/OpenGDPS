package com.fractalmines.database;

import com.fractalmines.database.providers.DatabaseProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Database {
    @Getter @Setter
    private DatabaseProvider activeProvider;
}
