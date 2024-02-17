package com.polito.qa.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

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
	public String checkDB(@RequestParam(name = "apipath") String apipath)
			throws MalformedURLException, IOException {
		String out = new Scanner(new URL(apipath).openStream(), "UTF-8").useDelimiter("\\A").next();
		return out;
	}
	
    @PostMapping("/ping")
    public String ping(@RequestBody String address) throws InterruptedException {
        try {
        
        	Process process = Runtime.getRuntime().exec(new String[] {"sh", "-c", "ping -c 3 " + address});

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Ping result for " + address + ":\n" + output.toString();
            } else {
                return "Error executing ping command. Exit code: " + exitCode;
            }
            
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @GetMapping(path = "/crypto/sha256")
    @ResponseBody
    public String getSha256(HttpServletRequest request) throws NoSuchAlgorithmException {
        String secret = SECRETS[new Random().nextInt(SECRETS.length)];
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
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(secret.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
    
}
