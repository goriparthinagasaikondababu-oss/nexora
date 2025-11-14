package org.ts.dao;

import org.ts.config.MySQL;
import org.ts.model.User;

import java.sql.*;

public class UserDAO {
    private final Connection connection;
    public UserDAO(){
        this.connection = MySQL.getConnection();
    }

    public String getUserNameById(String id){
        try {
            PreparedStatement query = this.connection.prepareStatement("SELECT username FROM users WHERE id=?;");
            query.setString(1, id);
            ResultSet result = query.executeQuery();
            result.next();
            return result.getString("username");
        } catch (SQLException e){
            return "";
        }
    }

    public User getUserByUserName(String username) {
        try {
            PreparedStatement query = this.connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            query.setString(1, username);
            ResultSet result = query.executeQuery();
            result.next();
            return new User(result.getString("id"), result.getString("username"), result.getString("email"), result.getString("fullname"), result.getString("password"));
        } catch (SQLException e){
            return null;
        }
    }

    public User getUserById(String id) {
        try {
            PreparedStatement query = this.connection.prepareStatement("SELECT * FROM users WHERE id=?;");
            query.setString(1, id);
            ResultSet result = query.executeQuery();
            result.next();
            User user = new User(result.getString("id"), result.getString("username"), result.getString("email"), result.getString("fullname"), result.getString("password"));
            user.setJoinedOn(result.getDate("created_on").toLocalDate());
            return user;
        } catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }
    public boolean save(User user) throws SQLException{
            PreparedStatement query = this.connection.prepareStatement("INSERT INTO users (id, username, email, fullname, password) VALUES(?, ?, ?, ?, ?);");
            query.setString(1, user.getId());
            query.setString(2, user.getUserName());
            query.setString(3, user.getEmail());
            query.setString(4, user.getFullName());
            query.setString(5, user.getPassword());
            int rowInserted = query.executeUpdate();
            return rowInserted > 0;
    }
}
