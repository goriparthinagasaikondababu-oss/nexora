package org.ts.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Post {
    private ObjectId _id;
    private String content;
    private String authorId;
    private Integer likesCount;
    private List<String> likedBy;
    private Integer commentsCount;
    private List<ObjectId> commentsIds;
    private LocalDateTime createdAt;
    private List<String> hashtags;

    public Post(String content, String authorId, List<String> hashtags) {
        this.content = content;
        this.authorId = authorId;
        this.likesCount = 0;
        this.likedBy = new ArrayList<String>();
        this.commentsCount = 0;
        this.commentsIds = new ArrayList<ObjectId>();
        this.createdAt = LocalDateTime.now();
        this.hashtags = hashtags;
    }

    public Post() {

    }

    public ObjectId getId() {
        return this._id;
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public Integer getLikesCount() {
        return this.likesCount;
    }

    public Integer getCommentsCount() {
        return this.commentsCount;
    }

    public List<ObjectId> getCommentsIds() {
        return this.commentsIds;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void setCommentsIds(List<ObjectId> commentsIds) {
        this.commentsIds = commentsIds;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }
}
