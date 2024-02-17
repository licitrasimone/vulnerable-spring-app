package com.polito.qa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polito.qa.exception.AnswerNotFoundException;
import com.polito.qa.exception.QuestionNotFoundException;
import com.polito.qa.exception.ValidationException;
import com.polito.qa.model.Answer;
import com.polito.qa.model.VoteRequest;
import com.polito.qa.repository.AnswerRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class AnswerController {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerController(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
    
    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<Answer>> listAnswersOf(@PathVariable int questionId) {
        try {
            List<Answer> answers = answerRepository.listAnswersOf(questionId);
            return ResponseEntity.ok(answers);
        } catch (QuestionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<Void> createAnswerForQuestion(
            @PathVariable int questionId,
            @RequestBody Answer answerRequest) {
        try {
            answerRepository.createAnswerForQuestion(answerRequest, questionId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (QuestionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
    
    @PutMapping("/answers/{id}")
    public ResponseEntity<Void> updateAnswer(
            @PathVariable int id,
            @RequestBody Answer answerRequest
    ) {
        try {
            answerRepository.updateAnswer(answerRequest, id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AnswerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
    
    @PostMapping("/answers/{id}/vote")
    public ResponseEntity<Void> voteAnswer(
            @PathVariable int id,
            @RequestBody VoteRequest vote
    ) {
        try { 
        	answerRepository.voteAnswer(id, vote);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

}
