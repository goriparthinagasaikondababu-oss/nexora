package org.ts.ui;

import org.bson.types.ObjectId;
import org.ts.utils.Console;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PostUI {
    private ObjectId _id;
    private String content;
    private String authorName;
    private Integer likesCount;
    private List<String> likedBy;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private List<String> hashtags;

    public ObjectId getId() {
        return _id;
    }

    public PostUI setId(ObjectId _id) {
        this._id = _id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostUI setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public PostUI setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public PostUI setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
        return this;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public PostUI setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PostUI setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public PostUI setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
        return this;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public PostUI setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
        return this;
    }

    private String formatTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return seconds + "s ago";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + "m ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        long days = hours / 24;
        return days + "d ago";
    }

    public String toString(){

         return String.format("👤 %-15s 🕒 %s\n%s\n💬 %-3d ❤️ %-3d\n%s\n%s", this.authorName, this.formatTimeAgo(this.createdAt), this.getContent(), this.commentsCount, this.likesCount, String.join(", ", this.hashtags), Console.divider);
    }
}
