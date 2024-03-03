## Instruction to execute exploit

- XXE Vulnerability: 
```
$ pyhton3 expolit-xxe.py
```

- Cookie Poisoning Vulnerability: 
```
$ pyhton3 cookiepois.py
```
- Unsalted Hash Vulnerability: 
```
$ python3 dictionaryAttack/expoit-cryptohash.py [hashvalue] [dict_db]
```
- Insecure Deserialization Vulnerability: 
    1. insert *Deserial/exploit-deserial.txt* as  Main class in *main/java/com*
    2. change the values ​​according to your needs
    3. run the main function and save the serialized value in base64
    4. take burpsuite, intercept the login and replace the csrf value with the value given by main
    5. now you execute a reverse shell attack

- Command Injection Vulenrability:
insert the values ​​contained in the CommandInjection/comminj.txt file into the api/secvices/ping API

- SQL Injection: 
intercept the login request with burp suite and enter the value contained in the file in the 'username' field and the value '12345678' in the password field

- SSRF Vulnerability: 
intercept api/services/checkapi and send another URL

- Sensitive Information Vulnerability: 
execute an expolit that permit to have a reverse shell and read log files

> Note: You can find the screenshots to understand the concept better



