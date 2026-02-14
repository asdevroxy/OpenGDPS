package com.fractalmines.database.mongo;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;

@NoArgsConstructor
@Getter
@Setter
public class Account {
    private long accountID = -1;
    private String username;
    private String email;
    private String legacyPassword;
    private String gjp2;
    private String secret;
    private @Nullable String steamId;
    private boolean active = true;
    private boolean banned = false;
}
