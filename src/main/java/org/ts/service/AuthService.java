package org.ts.service;

import org.mindrot.jbcrypt.BCrypt;
import org.ts.dao.UserDAO;
import org.ts.exception.DuplicateEmailException;
import org.ts.exception.DuplicateUsernameException;
import org.ts.exception.UnauthorizedException;
import org.ts.exception.UserNotFoundException;
import org.ts.model.User;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

public class AuthService {
    private final UserDAO userDAO;
    public AuthService(){
        this.userDAO = new UserDAO();
    }
    public User login(String username, String password) throws UserNotFoundException, UnauthorizedException {
        User user = this.userDAO.getUserByUserName(username);
        if(user==null){
            throw new UserNotFoundException("Username doesn't exist.");
        }
        String storedPassword = user.getPassword();
        if(!BCrypt.checkpw(password, storedPassword)){
            throw new UnauthorizedException("Invalid credentials.");
        }
        return user;

    }
    public User signUp(String username, String email, String fullName, String password) throws SQLException, DuplicateUsernameException, DuplicateEmailException {
        String id = UUID.randomUUID().toString();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(id, username, email, fullName, hashedPassword);
        try{
            if(!this.userDAO.save(user)){
                throw new RuntimeException("Failed to sign you up, please try again.");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            String message = e.getMessage().toLowerCase();

            if (message.contains("username")) {
                throw new DuplicateUsernameException("Username already exists. Try different username.");
            } else if (message.contains("email")) {
                throw new DuplicateEmailException("Email already registered.");
            } else {
                throw new RuntimeException(message);
            }
        }
        return user;
    }
}
