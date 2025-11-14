package org.ts.dao;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.ts.config.MongoDB;
import org.ts.model.Comment;
import org.ts.model.Post;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentDAO {
    private final MongoCollection<Document> collection;
    public CommentDAO(){
        this.collection = MongoDB.getDatabase().getCollection("comments");
    }
    public List<Comment> getComments(ObjectId postOrCommentId, int page, int pageSize){
        int skip = (page - 1) * pageSize;
        List<Comment> comments = new ArrayList<>();
        try (MongoCursor<Document> cursor = this.collection.find(Filters.eq("postOrCommentId", postOrCommentId)).sort(new Document("createdAt", -1)).skip(skip).limit(pageSize).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Comment comment = mapDocumentToComment(doc);
                comments.add(comment);
            }
        }
        return comments;
    }

    public ObjectId save(ClientSession session, Comment comment) throws NullPointerException{
        Document document = new Document()
                .append("text", comment.getText())
                .append("authorId", comment.getAuthorId())
                .append("likesCount", comment.getLikesCount())
                .append("commentsCount", comment.getSubCommentsCount())
                .append("commentsIds", comment.getSubCommentsIds())
                .append("createdAt", comment.getCreatedAt())
                .append("postOrCommentId", comment.getPostOrCommentId());
        try {
            InsertOneResult result = this.collection.insertOne(session, document);
            return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue();
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public Comment mapDocumentToComment(Document doc){
        Comment comment = new Comment();
        comment.setId(doc.getObjectId("_id"));
        comment.setText(doc.getString("text"));
        comment.setAuthorId(doc.getString("authorId"));
        comment.setPostOrCommentId(new ObjectId(doc.getString("postOrCommentId")));
        comment.setLikesCount(doc.getInteger("likesCount", 0));
        comment.setSubCommentsCount(doc.getInteger("subCommentsCount", 0));
        comment.setSubCommentsIds(doc.getList("subCommentsIds", ObjectId.class, new ArrayList<>()));
        LocalDateTime createdAt = doc.getDate("createdAt")
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        comment.setCreatedAt(createdAt);
        return comment;
    }
}
