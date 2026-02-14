package com.fractalmines.database.providers.sub;

import com.fractalmines.database.mongo.Comment;

import java.util.List;

public interface CommentProvider {
    List<Comment> getAccountComments(long accountID, int page);
    void addAccountComment(long accountID, Comment comment);
    long getTotalAccountComments(long accountID);
}
