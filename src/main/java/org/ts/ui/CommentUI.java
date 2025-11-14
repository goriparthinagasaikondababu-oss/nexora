package org.ts.ui;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommentUI {
    private ObjectId _id;
    private String authorName;
    private String text;
    private LocalDateTime createdAt;
    private Integer likesCount;
    private Integer subCommentsCount;

    public ObjectId getId() {
        return _id;
    }

    public CommentUI setId(ObjectId _id) {
        this._id = _id;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public CommentUI setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getText() {
        return text;
    }

    public CommentUI setText(String text) {
        this.text = text;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CommentUI setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public CommentUI setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
        return this;
    }

    public Integer getSubCommentsCount() {
        return subCommentsCount;
    }

    public CommentUI setSubCommentsCount(Integer subCommentsCount) {
        this.subCommentsCount = subCommentsCount;
        return this;
    }

    public String toString() {
        return String.format("[%s] %s: %s", createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE), authorName, text);
    }
}
