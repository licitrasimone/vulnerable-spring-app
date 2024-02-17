package com.polito.qa.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.polito.qa.model.Comment;


@Repository
public class CommentRepository {

	private final DataSource dataSource;

    @Autowired
    public CommentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	public List<Comment> listComments() {
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comment");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String text = resultSet.getString("text");

                Comment comment = new Comment(id, text);
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

	public void addComment(String comment) {
        String sql = "INSERT INTO comment(text) VALUES (?)";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment);


            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserimento della commento non riuscito, nessuna riga modificata.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
	}
	
}
