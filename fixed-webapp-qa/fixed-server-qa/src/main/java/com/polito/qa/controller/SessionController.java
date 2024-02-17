package com.polito.qa.controller;


import java.io.IOException;

import java.io.StringReader;

import java.util.Collections;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.polito.qa.controller.encdec.EncDec;
import com.polito.qa.model.CSRFToken;
import com.polito.qa.model.User;
import com.polito.qa.model.UserResponse;
import com.polito.qa.repository.SessionRepository;
import com.polito.qa.utils.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class SessionController {
	
    private final SessionRepository sessionRepository;
    private static final String COOKIE_NAME = "spoof_auth";
    
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
	@PostMapping
	@ResponseBody
    public ResponseEntity<?> login(@RequestBody User loginRequest, 
    		HttpServletRequest request, 
    		HttpServletResponse response,
    		@CookieValue(value = COOKIE_NAME, required = false) String cookieValue
    		) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String csrf = loginRequest.getCsrf();
        

        CSRFToken token = (CSRFToken) SerializationUtils.deserialize(csrf);
    	System.out.println(token);
        
        UserResponse user = null;
        
        try {
        	if(StringUtils.isEmpty(cookieValue)) {
	        	if ((user = sessionRepository.getUser(username, password)) == null) {
	            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore di autenticazione");
	            } 
	        
	            String newCookieValue = EncDec.encode(username);
	            Cookie newCookie = new Cookie(COOKIE_NAME, newCookieValue);
	            newCookie.setPath("/");
	            newCookie.setHttpOnly(false);
	            response.addCookie(newCookie);
	            
	            logger.info("Login with: " + username + ":" + password); 
	            
        	}else {
        		String cookieUsername = EncDec.decode(cookieValue);

        		if((user = sessionRepository.existUser(cookieUsername)) == null) {
        			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore di autenticazione");
        		}
        	}
        	
        	request.getSession().setAttribute("user", user);
        	
            return ResponseEntity.status(HttpStatus.OK).body(user);
            
        }catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore");
        }
        
    }
	
	@GetMapping("/current")
    public ResponseEntity<?> getCurrentSession(HttpServletRequest request, HttpServletResponse response) {
        UserResponse currentUser = (UserResponse) request.getSession().getAttribute("user");
        if (currentUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Not authenticated"));
        }
    }

    @DeleteMapping("/current")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        request.getSession().invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }

    @PostMapping(value = "/online", consumes = MediaType.APPLICATION_XML_VALUE)
    public String onlineUser(@RequestBody String body)
            throws ParserConfigurationException, SAXException, IOException {

    	
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); // Disabilita l'elaborazione delle entità esterne
        dbFactory.setNamespaceAware(true);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(body));
        Document doc = dBuilder.parse(inputSource);

        return doc.getElementsByTagName("username").item(0).getTextContent();
    }

	@GetMapping("/csrf")
	public String getCSRFToken() {
		CSRFToken token = new CSRFToken();
		return SerializationUtils.serialize(token);
	}


}
