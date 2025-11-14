package org.ts.service;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import org.bson.types.ObjectId;
import org.ts.config.MongoDB;
import org.ts.dao.CommentDAO;
import org.ts.dao.PostDAO;
import org.ts.dao.UserDAO;
import org.ts.model.Comment;
import org.ts.model.Post;
import org.ts.model.User;
import org.ts.ui.CommentUI;
import org.ts.ui.PostUI;
import org.ts.ui.UserUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedService {
    private final PostDAO postDAO;
    private final UserDAO userDAO;
    private final CommentDAO commentDAO;
    public FeedService(){
        this.postDAO = new PostDAO();
        this.userDAO = new UserDAO();
        this.commentDAO = new CommentDAO();
    }

    public List<PostUI> getPostsUtil(List<Post> posts){
        return posts.stream().map(post -> {
            return new PostUI()
                    .setId(post.getId())
                    .setAuthorName(this.userDAO.getUserNameById(post.getAuthorId()))
                    .setContent(post.getContent())
                    .setLikesCount(post.getLikesCount())
                    .setCommentsCount(post.getCommentsCount())
                    .setCreatedAt(post.getCreatedAt())
                    .setLikedBy(post.getLikedBy())
                    .setHashtags(post.getHashtags());
        }).collect(Collectors.toList());
    }

    public List<PostUI> retrievePosts(int pageNumber, int postsPerPage){
        return this.getPostsUtil(this.postDAO.getPosts(pageNumber, postsPerPage));
    }

    public PostUI getPostDetails(ObjectId id){
        Post post =  this.postDAO.getPostById(id);
        return new PostUI()
                .setId(post.getId())
                .setAuthorName(this.userDAO.getUserNameById(post.getAuthorId()))
                .setContent(post.getContent())
                .setLikesCount(post.getLikesCount())
                .setCommentsCount(post.getCommentsCount())
                .setCreatedAt(post.getCreatedAt())
                .setLikedBy(post.getLikedBy())
                .setHashtags(post.getHashtags());
    }

    public boolean savePost(String authorId, String content, List<String> hashtags){
        Post post = new Post(content, authorId, hashtags);
        return this.postDAO.save(post);
    }

    public void updatePostLikeCount(ObjectId postId, String authorId){
        this.postDAO.toggleLike(postId, authorId);
    }

    public boolean addCommentToPost(ObjectId postId, String authorId, String commentText){
        Comment comment = new Comment();
        comment.setPostOrCommentId(postId);
        comment.setAuthorId(authorId);
        comment.setText(commentText);
        ClientSession session = MongoDB.getClient().startSession();
        boolean isSaved = false;
        try {
            session.startTransaction();
            // Perform operations within the transaction, passing the session to each operation
            ObjectId commentId = this.commentDAO.save(session, comment);
            if(commentId == null){
                throw new RuntimeException("Failed to create comment.");
            }
            this.postDAO.addCommentIdToPost(session, postId, commentId);
            session.commitTransaction();
            isSaved = true;
        } catch (Exception e) {
            session.abortTransaction();
        } finally {
            session.close();
        }
        return isSaved;
    }

    public List<CommentUI> getCommentsForPostOrComment(ObjectId postOrCommentId, int pageNumber, int pageSize){
        return this.commentDAO.getComments(postOrCommentId, pageNumber, pageSize).stream().map(comment -> {
            return new CommentUI()
                    .setId(comment.getId())
                    .setAuthorName(this.userDAO.getUserNameById(comment.getAuthorId()))
                    .setText(comment.getText())
                    .setLikesCount(comment.getLikesCount())
                    .setSubCommentsCount(comment.getSubCommentsCount())
                    .setCreatedAt(comment.getCreatedAt());
        }).collect(Collectors.toList());
    }

    public UserUI getUserDetails(String userId) {
        User user = this.userDAO.getUserById(userId);
        if(user == null){
            return null;
        }
        return new UserUI()
                .setUserName(user.getUserName())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setJoinedOn(user.getJoinedOn());
    }

    public List<PostUI> getPostForUser(String id, int pageNumber, int postsPerPage) {
        return this.getPostsUtil(this.postDAO.getPosts(id, pageNumber, postsPerPage));
    }

    public int getCountOfPostsByUser(String id) {
        return this.postDAO.getPosts(id).size();
    }
}
