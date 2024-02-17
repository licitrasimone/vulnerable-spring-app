# Run Project 
### SERVER
```
$ mvn spring-boot:run
```

### CLIENT
```
$ npm install
$ npm run dev
```

# Vulnerability

* 2 SQL Injections (A03:2021 Injection)
* 1 Cookie poisoning (A01:2021 Broken Access Control)
* 3 XSS Injections (A03:2021 Injection)
* 1 SSRF (A10:2021 Server-Side Request Forgery)
* 1 Sensitive Information into Log File (A09:2021 Logging and Monitoring
Failure)
* 1 XEE (A05:2021 Security Misconfiguration)
* 1 Command Injection (A03:2021 Injection)
* 1 Use of a One-Way Hash without a Salt (A02:2021 Cryptographic Failures)
* 1 Insecure Deserialization (A08:2021 Software and Data Integrity Failures)






