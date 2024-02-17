package com.polito.qa.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polito.qa.model.UserResponse;

@Service
public class SessionRepository {
	
	private final DataSource dataSource;

    @Autowired
    public SessionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	public UserResponse getUser(String email, String password) {
		System.out.println(email);
        try (Connection connection = dataSource.getConnection();) {
        	String sql = "SELECT * FROM user WHERE email = '" + email + "'";
            try (Statement statement = connection.createStatement()) {
           
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        

                        if (password.equals(storedPassword)) {
                            return new UserResponse(
                                    resultSet.getInt("id"),
                                    resultSet.getString("email"),
                                    resultSet.getString("name")
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return null; 
    }

	public UserResponse existUser(String username) {
        try (Connection connection = dataSource.getConnection();) {
           
            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            
            if (resultSet.next()) {
                return new UserResponse(
                    resultSet.getInt("id"),
                    resultSet.getString("email"),
                    resultSet.getString("name")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        
        return null;
	}
	
	/*
    private String hashPassword(String password, String salt) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password + salt);
    }*/

}
