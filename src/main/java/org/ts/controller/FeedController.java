package org.ts.controller;

import org.bson.types.ObjectId;
import org.ts.exception.UnauthorizedException;
import org.ts.model.User;
import org.ts.service.FeedService;
import org.ts.ui.CommentUI;
import org.ts.ui.PostUI;
import org.ts.ui.UserUI;
import org.ts.utils.Console;

import java.util.*;

public class FeedController {
    public final FeedService feedService;
    public User currentUser;
    public FeedController(){
        this.feedService = new FeedService();
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public boolean run() throws UnauthorizedException{
        if(this.currentUser==null){
            throw new UnauthorizedException("Please login to continue.");
        }
        int currentPage = 0;
        final int POSTS_PER_PAGE = 5;
        String prompt = "";
        Map<Integer, ObjectId> idxIdMap = new HashMap<>();
        System.out.println("Recent Posts");
        List<PostUI> posts = new ArrayList<>();
        boolean shouldRefreshPosts = true;

        do{
            if(shouldRefreshPosts) {
                    System.out.println(Console.divider);
                    posts = this.feedService.retrievePosts(currentPage, POSTS_PER_PAGE);
                    if(posts.isEmpty()) {
                        System.out.println("No posts to show.");
                    } else {
                        posts.forEach(System.out::println);
                        idxIdMap = new HashMap<>();
                        for (int i = 0; i < posts.size(); i++) {
                            PostUI post = posts.get(i);
                            idxIdMap.put(i+1, post.getId());
                        }
                        System.out.println("Enter post number to interact with post.");
                        if (currentPage > 0) {
                            System.out.println("[P] Previous - load previous posts");
                        }
                        System.out.println("[N] Next - load more posts");
                    }
                System.out.println("[C] Create - create new post");
                System.out.println("[R] Refresh - load recent posts");
                System.out.println("[V] View my profile");
                System.out.println("[E] Exit");
                shouldRefreshPosts = false;
            }
            prompt = Console.readInput(">>> ");
            if(prompt.equalsIgnoreCase("c")){
                this.askPostDetails();
                currentPage = 0;
                shouldRefreshPosts = true;
            } else if(prompt.equalsIgnoreCase("p")){
                if(currentPage>0) {
                    currentPage -= 1;
                }
                shouldRefreshPosts = true;
            } else if (prompt.equalsIgnoreCase("n")) {
                currentPage += 1;
                shouldRefreshPosts = true;
            } else if (prompt.equalsIgnoreCase("r")) {
                shouldRefreshPosts = true;
                currentPage += 0;
            } else if (prompt.equalsIgnoreCase("v")) {
                this.showProfile(currentUser.getId());
                shouldRefreshPosts = true;
            } else if(prompt.equalsIgnoreCase("e")){
                System.exit(0);
            } else{
                try {
                    int postNumber = Integer.parseInt(prompt);
                    if (postNumber >= 1 && postNumber <= posts.size()) {
                        if(idxIdMap.get(postNumber) != null){
                            this.showPost(idxIdMap.get(postNumber));
                            shouldRefreshPosts = true;
                        }
                    } else {
                        System.out.println("Invalid post number.");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid option, please try again.");
                }
            }
        }while (true);
    }

    public void askPostDetails(){
        String content = Console.readInput("Enter post content\n>>> ");
        String prompt = Console.readInput("Do you want to any hashtags (Y/N)? ");
        List<String> hashtags = new ArrayList<>();
        if(prompt.equalsIgnoreCase("y")){
            hashtags = Arrays.stream(Console.readInput("Enter tags separated by comma(,): ").split(",")).toList();
        }
        boolean isSaved = this.feedService.savePost(this.currentUser.getId(), content, hashtags);
        if(isSaved){
            System.out.println("Post uploaded successfully.");
        } else{
            System.out.println("Failed to upload post, please try again.");
        }
    }

    public void showPost(ObjectId postId){
        PostUI post = null;
        String prompt = "";
        boolean shouldReloadPost = true;
        do{
            if(shouldReloadPost) {
                post = this.feedService.getPostDetails(postId);
                System.out.println(post);
                shouldReloadPost = false;
            }
            if(post != null) {
                if(post.getLikedBy().contains(this.currentUser.getId())){
                    System.out.println("[U] Unlike");
                }else {
                    System.out.println("[L] Like");
                }
                System.out.println("[C] Comment");
                System.out.println("[S] Show comments");
            } else {
                System.out.println("Oops! Post not available.");
            }
            System.out.println("[B] Back");
            System.out.println("[E] Exit");
            prompt = Console.readInput(">>> ");
            if(prompt.equalsIgnoreCase("l") || prompt.equalsIgnoreCase("u")){
                try {
                    this.feedService.updatePostLikeCount(postId, this.currentUser.getId());
                    if(prompt.equalsIgnoreCase("l"))
                        System.out.println("You've liked this post.");
                    else
                        System.out.println("You've unliked this post.");
                    shouldReloadPost = true;
                } catch (Exception e){
                    System.out.println("Failed to like.");
                }

            } else if(prompt.equalsIgnoreCase("c")){
                this.askPostComment(postId);
                shouldReloadPost = true;
            } else if(prompt.equalsIgnoreCase("s")){
                this.showComments(postId);
                shouldReloadPost = true;
            } else if(prompt.equalsIgnoreCase("e")){
                System.exit(0);
            } else if(prompt.equalsIgnoreCase("b")){
                break;
            } else{
                System.out.println("Invalid option, please try again.");
            }
        }while(true);
    }

    public void showComments(ObjectId postId){
        int currentPage = 0;
        final int COMMENTS_PER_PAGE = 5;
        String prompt = "";
        System.out.println("Recent Comments");
        List<CommentUI> comments = new ArrayList<>();
        boolean shouldRefreshComments = true;

        do{
            if(shouldRefreshComments) {
                comments = this.feedService.getCommentsForPostOrComment(postId, currentPage, COMMENTS_PER_PAGE);
                if(comments.isEmpty()) {
                    System.out.println("No comments to show.");
                } else {
                    comments.forEach(System.out::println);
                    if (currentPage > 0) {
                        System.out.println("[P] Previous - load previous comments");
                    }
                    System.out.println("[N] Next - load more comments");

                }
                System.out.println("[A] Add comment");
                System.out.println("[R] Refresh - load recent comments");
                System.out.println("[B] Back");
                System.out.println("[E] Exit");
                shouldRefreshComments = false;
            }
            prompt = Console.readInput(">>> ");
            if(prompt.equalsIgnoreCase("a")){
                this.askPostComment(postId);
                currentPage = 0;
                shouldRefreshComments = true;
            } else if(prompt.equalsIgnoreCase("p")){
                if(currentPage>0) {
                    currentPage -= 1;
                }
                shouldRefreshComments = true;
            } else if (prompt.equalsIgnoreCase("n")) {
                currentPage += 1;
                shouldRefreshComments = true;
            } else if (prompt.equalsIgnoreCase("r")) {
                shouldRefreshComments = true;
                currentPage += 0;
            } else if (prompt.equalsIgnoreCase("b")) {
                break;
            } else if(prompt.equalsIgnoreCase("e")){
                System.exit(0);
            } else{
                System.out.println("Invalid option, please try again.");
            }
        }while (true);
    }

    public void askPostComment(ObjectId postId){
        String comment = Console.readInput("Enter comment: ");

        boolean isSaved =this.feedService.addCommentToPost(postId, this.currentUser.getId(), comment);
        if(isSaved){
            System.out.println("Comment added successfully");
        } else{
            System.out.println("Failed to add comment, please try again.");
        }
    }

    public void showProfile(String userId){
        System.out.println(Console.divider);
        System.out.println("My Profile");
        System.out.println(Console.divider);
        UserUI user = this.feedService.getUserDetails(userId);
        System.out.println(user);
        System.out.println(Console.divider);
        System.out.println("Your Activity");
        System.out.println(Console.divider);
        int postCount = this.feedService.getCountOfPostsByUser(currentUser.getId());
        System.out.printf("Posts: %s\n", postCount);
        List<PostUI> posts = this.feedService.getPostForUser(currentUser.getId(), 0, 5);
        Console.readInput("Press any key to go back to feed.");
    }
}
