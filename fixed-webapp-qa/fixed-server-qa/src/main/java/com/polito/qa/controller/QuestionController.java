package com.polito.qa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polito.qa.exception.QuestionNotFoundException;
import com.polito.qa.model.Question;
import com.polito.qa.repository.QuestionRepository;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class QuestionController {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    
	@GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
		try {
			return ResponseEntity.ok(questionRepository.listQuestions());
		} catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
        try {
            Question question = questionRepository.getQuestionById(id);
            return ResponseEntity.ok(question);
        } catch (QuestionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	
	@PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody Question request) {
        try {
            Question question = new Question(0, request.getText(), request.getAuthor(), request.getDate());
            int questionId = questionRepository.addQuestion(question);
            return new ResponseEntity<>("Question added with ID: " + questionId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add question.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	

}
