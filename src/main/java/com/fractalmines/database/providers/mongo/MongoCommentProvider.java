package com.fractalmines.database.providers.mongo;

import com.fractalmines.database.mongo.Comment;
import com.fractalmines.database.providers.MongoProvider;
import com.fractalmines.database.providers.sub.CommentProvider;
import com.fractalmines.util.BsonUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MongoCommentProvider implements CommentProvider {
    private final MongoProvider provider;
    private MongoCollection<Comment> collection;
    private MongoCollection<Comment> accountCollection;

    public MongoCommentProvider(MongoProvider provider) {
        this.provider = provider;

        this.collection = provider.getDatabase().getCollection("comments", Comment.class);
        this.accountCollection = provider.getDatabase().getCollection("account_comments", Comment.class);
    }

    @Override
    public List<Comment> getAccountComments(long accountID, int page) {
        Bson filter = Filters.eq("accountID", accountID);

        List<Comment> data = new ArrayList<>();
        accountCollection.find(filter).sort(Sorts.descending("_id")).skip(page * 10).limit(10).forEach(data::add);

        return data;
    }

    @Override
    public void addAccountComment(long accountID, Comment comment) {
        comment.setId(provider.getNextIndex("account_comments"));
        accountCollection.insertOne(comment);
    }

    @Override
    public long getTotalAccountComments(long accountID) {
        return accountCollection.countDocuments(Filters.eq("accountID", accountID));
    }
}
