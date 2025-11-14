package org.ts.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private ObjectId _id;
    private ObjectId postOrCommentId; // back-reference
    private String authorId;
    private String text;
    private LocalDateTime createdAt;
    private Integer likesCount;
    private Integer subCommentsCount;
    private List<ObjectId> subCommentsIds;

    public Comment( String text, ObjectId postOrCommentId, String authorId) {
        this.text = text;
        this.postOrCommentId = postOrCommentId;
        this.authorId = authorId;
        this.createdAt = LocalDateTime.now();
        this.likesCount = 0;
        this.subCommentsCount = 0;
        this.subCommentsIds = new ArrayList<ObjectId>();
    }

    public Comment(){}

    public ObjectId getId() {
        return this._id;
    }

    public ObjectId getPostOrCommentId() {
        return this.postOrCommentId;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getText() {
        return this.text;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Integer getLikesCount() {
        return this.likesCount;
    }

    public Integer getSubCommentsCount() {
        return this.subCommentsCount;
    }

    public List<ObjectId> getSubCommentsIds() {
        return this.subCommentsIds;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public void setPostOrCommentId(ObjectId postOrCommentId) {
        this.postOrCommentId = postOrCommentId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public void setSubCommentsCount(Integer subCommentsCount) {
        this.subCommentsCount = subCommentsCount;
    }

    public void setSubCommentsIds(List<ObjectId> subCommentsIds) {
        this.subCommentsIds = subCommentsIds;
    }
}