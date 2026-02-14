package com.fractalmines.database.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Comment {
    private long id;
    private long accountID;

    private String comment;

    private long postDate;
    private int likes = 0;

    public boolean isSpam() {
        return likes <= -1; // TODO: this is debug. change to -3
    }
}
