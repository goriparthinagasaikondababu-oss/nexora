package org.ts.controller;

import org.ts.exception.DuplicateEmailException;
import org.ts.exception.DuplicateUsernameException;
import org.ts.exception.UnauthorizedException;
import org.ts.exception.UserNotFoundException;
import org.ts.model.User;
import org.ts.service.AuthService;
import org.ts.utils.Console;

public class AuthController {
    private final AuthService authService;
    public AuthController(){
        this.authService = new AuthService();
    }

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    public User run(){
        String prompt;
        System.out.println(Console.divider);
        System.out.printf("%-50s\n", "Welcome to nexora!\n");
        System.out.println(Console.divider);
        System.out.println("[L] - Login\n[S] - Sign Up\n[E] - Exit");
        System.out.println(Console.divider);

        User user;
        do{
            prompt = Console.readInput(">>> ");
            if(prompt.equalsIgnoreCase("l")) {
                user = this.getLoginDetails();
                break;
            } else if (prompt.equalsIgnoreCase("s")) {
                user = this.getSignUpDetails();
                break;
            } else if (prompt.equalsIgnoreCase("e")) {
                System.exit(0);
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }while (true);
        return user;
    }
    public User getLoginDetails(){
        System.out.println(Console.divider);
        System.out.println("LOGIN");
        User user = null;
        do {
            try {
                System.out.println(Console.divider);
                String username = Console.readInput("Username: ");
                String password = Console.readInput("Password: ");
                System.out.println(Console.divider);
                user = this.authService.login(username, password);
                System.out.printf("Login successful! Welcome back, %s\n", user.getFullName());
                return user;
            } catch (UserNotFoundException | UnauthorizedException e) {
                System.out.println("Invalid username or password. Please try again.");
                String prompt = Console.readInput("Do you want to retry (Y/N)? ");
                if (prompt.equalsIgnoreCase("n")) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                System.out.println(e); // TODO Log this
            } catch (Exception e) {
                System.out.println("Failed to login.");
                String prompt = Console.readInput("Do you want to retry (Y/N)? ");
                if (prompt.equalsIgnoreCase("n")) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                System.out.println(e);
            }
        }while(true);
    }
    public User getSignUpDetails(){
        System.out.println(Console.divider);
        System.out.println("SIGN UP");
        System.out.println(Console.divider);
        String username = Console.readInput("Username: ");
        String email = Console.readInput("Email: ");
        String fullName = Console.readInput("Full Name: ");
        String password = Console.readInput("Password: ");
        String confirmPassword;
        do {
            confirmPassword = Console.readInput("Confirm Password: ");
            if(password.equals(confirmPassword)){
                break;
            } else {
                System.out.println("Password and Confirm Password doesn't match.");
            }
        }while(true);
        System.out.println(Console.divider);
        User user;
        do{
            try{
                user = this.authService.signUp(username, email, fullName, password);
                System.out.printf("Account created successfully. Welcome %s\n", user.getFullName());
                return user;
            } catch(DuplicateUsernameException e){
                System.out.println("Username already taken, try different username.");
                username = Console.readInput("Username");
            } catch (DuplicateEmailException e){
                System.out.println("Email already exists, do you want to login ?");
                do{
                    System.out.println("Y. Login\nN. Re-enter email\nE. Exit");
                    String prompt = Console.readInput();
                    if(prompt.equalsIgnoreCase("Y")) {
                        return this.getLoginDetails();
                    } else if(prompt.equalsIgnoreCase("N")){
                        email = Console.readInput("Email: ");
                    } else if(prompt.equalsIgnoreCase("E")){
                        System.exit(0);
                    } else{
                        System.out.println("Invalid option, please try again with valid option.");
                    }
                }while(true);
            } catch (Exception e){
                System.out.println("Failed to sign you up.");
                String prompt = Console.readInput("Do you want to retry (Y/N)? ");
                if(prompt.equalsIgnoreCase("y")){
                    return  this.getSignUpDetails();
                } else {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                System.out.println(e); // TODO: Log this
            }
        }while(true);
    }
}
