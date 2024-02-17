package com.polito.qa.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class ServiceController {
	
	public static final String[] SECRETS = {"secret", "admin", "password", "123456", "passw0rd"};


	@PostMapping("/checkapi")
    public String checkDB(@RequestParam(name = "apipath") String apipath) {
        try {
            if (!isValidURL(apipath)) {
                return "Invalid API URL";
            }

            URL url = new URL(apipath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            connection.disconnect();

            return response.toString();
        } catch (MalformedURLException e) {
            return "Malformed URL: " + e.getMessage();
        } catch (IOException e) {
            return "Error accessing API: " + e.getMessage();
        }
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            System.out.println(url);
            return url.equals("http://127.0.0.1:3001/api/questions");
        } catch (Exception e) {
            return false;
        }
    }
	
	@PostMapping("/ping")
	public ResponseEntity<String> ping(@RequestBody String address) {
	    if (!isValidAddress(address)) {
	        return ResponseEntity.badRequest().body("Invalid address format.");
	    }

	    try {
	        ProcessBuilder processBuilder = new ProcessBuilder("ping", "-c", "3", address);
	        Process process = processBuilder.start();

	        StringBuilder output = new StringBuilder();
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                output.append(line).append("\n");
	            }
	        }

	        int exitCode = process.waitFor();
	        if (exitCode == 0) {
	            return ResponseEntity.ok("Ping result for " + address + ":\n" + output.toString());
	        } else {
	            return ResponseEntity.ok("Error executing ping command. Exit code: " + exitCode);
	        }

	    } catch (IOException | InterruptedException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
	    }
	}
	
	private boolean isValidAddress(String address) {
	    try {
	        InetAddress.getByName(address);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
    
    @GetMapping(path = "/crypto/sha256")
    @ResponseBody
    public String getSha256(HttpServletRequest request) throws NoSuchAlgorithmException {
        String secret = SECRETS[new SecureRandom().nextInt(SECRETS.length)];
        String hash = getHash(secret);
        request.getSession().setAttribute("sha256Secret", hash);
        return hash;
    }

    @PostMapping(path = "/crypto/sha256/verify")
    @ResponseBody
    public String verifySha256(@RequestBody String password, HttpServletRequest request) throws NoSuchAlgorithmException {
        String secret = (String) request.getSession().getAttribute("sha256Secret");

        if (secret != null && secret.equals(getHash(password))) {
            return "OK. You found the correct password.";
        }
        System.out.println("questo:"+secret);

        return "Error. Try Again";
    }
    
    public String getHash(String secret) throws NoSuchAlgorithmException {
    	SecureRandom random = new SecureRandom();
    	byte[] salt = new byte[16];
    	random.nextBytes(salt);
    	String str_salt = Base64.getEncoder().encodeToString(salt);
    	
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((str_salt + secret).getBytes());
        byte[] digest = md.digest();
        
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
    
}
