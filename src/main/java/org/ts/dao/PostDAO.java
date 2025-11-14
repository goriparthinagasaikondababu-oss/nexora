package org.ts.dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.ts.config.MongoDB;
import org.ts.model.Post;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    private MongoCollection<Document> collection;
    public PostDAO(){
        this.collection = MongoDB.getDatabase().getCollection("posts");
    }

    public List<Post> getPosts(){
        List<Post> posts = new ArrayList<>();
        try (MongoCursor<Document> cursor = this.collection.find().sort(new Document("createdAt", -1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Post post = new Post();
                post = mapDocumentToPost(doc);
                posts.add(post);
            }
        }
        return posts;
    }

    public List<Post> getPosts(int page, int pageSize) {
        int skip = (page - 1) * pageSize;
        List<Post> posts = new ArrayList<>();
        try (MongoCursor<Document> cursor = this.collection.find().sort(new Document("createdAt", -1)).skip(skip).limit(pageSize).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Post post = mapDocumentToPost(doc);
                posts.add(post);
            }
        }
        return posts;
    }

    public List<Post> getPosts(String authorId){
        List<Post> posts = new ArrayList<>();
        try (MongoCursor<Document> cursor = this.collection.find(Filters.eq("authorId", authorId)).sort(new Document("createdAt", -1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Post post = mapDocumentToPost(doc);
                posts.add(post);
            }
        }
        return posts;
    }

    public List<Post> getPosts(String authorId, int page, int pageSize) {
        int skip = (page - 1) * pageSize;
        List<Post> posts = new ArrayList<>();
        try (MongoCursor<Document> cursor = this.collection.find(Filters.eq("authorId", authorId)).sort(new Document("createdAt", -1)).skip(skip).limit(pageSize).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Post post = mapDocumentToPost(doc);
                posts.add(post);
            }
        }
        return posts;
    }

    public Post getPostById(ObjectId _id){
        Document doc = this.collection.find(Filters.eq("_id", _id)).first();
        if (doc == null) {
            return null;
        }
        return mapDocumentToPost(doc);
    }

    public boolean save(Post post){
        Document document = new Document()
                .append("content", post.getContent())
                .append("authorId", post.getAuthorId())
                .append("likesCount", post.getLikesCount())
                .append("likedBy", post.getLikedBy())
                .append("commentsCount", post.getCommentsCount())
                .append("commentsIds", post.getCommentsIds())
                .append("hashtags", post.getHashtags())
                .append("createdAt", post.getCreatedAt());
        InsertOneResult result = this.collection.insertOne(document);
        return result.getInsertedId() != null;
    }
    private Post mapDocumentToPost(Document doc) {
        Post post = new Post();
        post.setId(doc.getObjectId("_id"));
        post.setContent(doc.getString("content"));
        post.setAuthorId(doc.getString("authorId"));
        post.setLikesCount(doc.getInteger("likesCount", 0));
        post.setLikedBy(doc.getList("likedBy", String.class, new ArrayList<>()));
        post.setCommentsCount(doc.getInteger("commentsCount", 0));
        post.setCommentsIds(doc.getList("commentsIds", ObjectId.class, new ArrayList<ObjectId>()));
        LocalDateTime createdAt = doc.getDate("createdAt")
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        post.setCreatedAt(createdAt);
        post.setHashtags(doc.getList("hashtags", String.class, new ArrayList<>()));
        return post;
    }

    public void addCommentIdToPost(ClientSession session, ObjectId postId, ObjectId commentId) {
        this.collection.updateOne(
                session,
                Filters.eq("_id", postId),
                Updates.combine(
                    Updates.push("commentsIds", commentId),
                    Updates.inc("commentsCount", 1)
                )
        );
    }

    public void incrementLikeCount(ObjectId postId) {
        this.collection.updateOne(Filters.eq("_id", postId), Updates.inc("likesCount", 1));
    }

    public void toggleLike(ObjectId postId, String userId){
        Document document = this.collection.find(Filters.eq("_id", postId)).first();
        List<String> likedBy = document.getList("likedBy", String.class, new ArrayList<>());
        int likeMagnitude = 1;
        if(likedBy.contains(userId)){
            this.collection.updateOne(Filters.eq("_id", postId), Updates.combine(Updates.inc("likesCount", -1), Updates.pull("likedBy", userId)));
        } else {
            this.collection.updateOne(Filters.eq("_id", postId), Updates.combine(Updates.inc("likesCount", 1), Updates.push("likedBy", userId)));
        }
    }
}
