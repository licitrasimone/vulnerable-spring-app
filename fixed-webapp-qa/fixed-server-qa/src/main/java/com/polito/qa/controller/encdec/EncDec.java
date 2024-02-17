package com.polito.qa.controller.encdec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Hex;

/**
 * EncDec Class
 * Fixed encode/decode methods: with salt + hash verify
 * @author Simone Licitra
 */
public class EncDec {

  private static final String SALT = RandomStringUtils.randomAlphabetic(10);

  public static String encode(final String value) {
      if (value == null) {
          return null;
      }

      try {
          String saltedValue = value.toLowerCase() + SALT;
          byte[] hashedBytes = hashValue(saltedValue.getBytes(StandardCharsets.UTF_8));
          return base64Encode(hashedBytes) + "|" + value;
      } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
          return null;
      }
  }

  public static String decode(final String encodedValue) throws IllegalArgumentException {
      if (encodedValue == null) {
          return null;
      }

      String[] split = encodedValue.split("|");
      if (split.length != 2) {
          throw new IllegalArgumentException("Invalid encoded value");
      }

      String base64EncodedHash = split[0];
      String originalValue = split[1];

      byte[] decodedBytes = base64Decode(base64EncodedHash);
      String decodedValue = new String(decodedBytes, StandardCharsets.UTF_8);
      String saltedValue = originalValue.toLowerCase() + SALT;

      try {
          byte[] hashedBytes = hashValue(saltedValue.getBytes(StandardCharsets.UTF_8));
          String calculatedHash = base64Encode(hashedBytes);
          
          if (!MessageDigest.isEqual(calculatedHash.getBytes(), base64EncodedHash.getBytes())) {
              throw new IllegalArgumentException("Hash verification failed");
          }
      } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
          return null;
      }

      return originalValue;
  }

  private static byte[] hashValue(byte[] value) throws NoSuchAlgorithmException {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(value);
  }

  private static String base64Encode(byte[] bytes) {
      return Base64.getEncoder().encodeToString(bytes);
  }

  private static byte[] base64Decode(String encodedString) {
      return Base64.getDecoder().decode(encodedString);
  }
}