package com.polito.qa.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.polito.qa.exception.AnswerNotFoundException;
import com.polito.qa.model.Answer;
import com.polito.qa.model.VoteRequest;



@Repository
public class AnswerRepository {
	private final DataSource dataSource;

    @Autowired
    public AnswerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<Answer> listAnswersOf(int questionId) {
        List<Answer> answers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM answer WHERE questionId = ?")) {

            statement.setInt(1, questionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String text = resultSet.getString("text");
                    String author = resultSet.getString("author");
                    String date = resultSet.getString("date");
                    int score = resultSet.getInt("score");

                    Answer answer = new Answer(id, text, author, date, score);
                    answers.add(answer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }
 
	
	public void createAnswerForQuestion(Answer answer, int questionId) {
        String sql = "INSERT INTO answer(text, author, date, score, questionId) VALUES (?, ?, DATE(?), ?, ?)";

        try (Connection connection = dataSource.getConnection(); 
        		PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, answer.getText());
            statement.setString(2, answer.getAuthor());
            statement.setString(3, answer.getDate());
            statement.setInt(4, answer.getScore());
            statement.setInt(5, questionId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding answer to the database.", e);
        }
    }
	
	public void updateAnswer(Answer answerRequest, int answerId) {
        String sql = "UPDATE answer SET text = ?, author = ?, date = DATE(?), score = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, answerRequest.getText());
            statement.setString(2, answerRequest.getAuthor());
            statement.setString(3, answerRequest.getDate());
            statement.setInt(4, answerRequest.getScore());
            statement.setInt(5, answerId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated <= 0) {
                throw new AnswerNotFoundException("Answer not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating answer", e);
        }
    }

	public void voteAnswer(int id, VoteRequest voteRequest) {
		try (Connection connection = dataSource.getConnection()) {
            int delta = "upvote".equals(voteRequest.getVote()) ? 1 : -1;
            String sql = "UPDATE answer SET score = score + ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, delta);
                statement.setInt(2, id);
                int updatedRows = statement.executeUpdate();
                if (updatedRows == 1) {
                    return;
                } else {
                    throw new RuntimeException("Error in casting a vote for answer #" + id);
                }
            }
        } catch (SQLException e) {
        	 throw new RuntimeException("Error voting answer", e);
        }
	
	}

}
