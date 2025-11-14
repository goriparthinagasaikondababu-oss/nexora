package org.ts.controller;

import org.ts.exception.UnauthorizedException;
import org.ts.model.User;
import org.ts.service.AuthService;
import org.ts.utils.Console;


public class MainController {
    private final FeedController feedController;
    private final AuthController authController;
    public MainController(){
        this.authController = new AuthController(new AuthService());
        this.feedController = new FeedController();
    }
    public void run(){
        User user = this.authController.run();
        if(user != null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            System.out.println("Loading feed...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            this.feedController.setCurrentUser(user);
            try{
                this.feedController.run();
            } catch (UnauthorizedException e){
                System.out.println(e);
            }

        }
    }
}
