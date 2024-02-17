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

import com.polito.qa.model.Question;

@Repository
public class QuestionRepository {
	
	private final DataSource dataSource;

    @Autowired
    public QuestionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	public List<Question> listQuestions() {
        List<Question> questions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM question");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String text = resultSet.getString("text");
                String author = resultSet.getString("author");
                String date = resultSet.getString("date");

                Question question = new Question(id, text, author, date);
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

	public Question getQuestionById(int id) {
		Question question = null;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM question WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        question = new Question(
                                resultSet.getInt("id"),
                                resultSet.getString("text"),
                                resultSet.getString("author"),
                                resultSet.getString("date")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return question;
	}
	
	public int addQuestion(Question question) {
        String sql = "INSERT INTO question(text, author, date) VALUES (?, ?, DATE(?))";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, question.getText());
            statement.setString(2, question.getAuthor());
            statement.setString(3, question.getDate());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserimento della domanda non riuscito, nessuna riga modificata.");
            }

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserimento della domanda non riuscito, nessuna chiave generata.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return -1; 
    }
}
