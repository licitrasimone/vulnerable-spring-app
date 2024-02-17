package com.polito.qa.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * SerializationUtils Class
 * Fixed version of serial/deserial method: check object's class
 * @author Simone Licitra
 */
public class SerializationUtils {
	public static String serialize(Object item) {
	    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    final ObjectOutputStream objectOutputStream;
	    try {
	        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	        
	        objectOutputStream.writeUTF(item.getClass().getName());
	        
	        objectOutputStream.writeObject(item);
	        objectOutputStream.close();
	        
	        byte[] bytes = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
	        return new String(bytes, StandardCharsets.US_ASCII);
	        
	    } catch (IOException e) {
	        throw new Error(e);
	    }
	}
	
    private static final String ALLOWED_CLASS = "com.polito.qa.model.CSRFToken";

    public static Object deserialize(String data) {
        try {
            byte[] bytes = Base64.getDecoder().decode(data);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            
            
            String className = objectInputStream.readUTF();
            if (!className.equals(ALLOWED_CLASS)) {
                throw new SecurityException("Deserialization of this class is not allowed.");
            }

            Object obj = objectInputStream.readObject();
            objectInputStream.close();
            return obj;
        } catch (IOException | ClassNotFoundException | SecurityException e) {
            throw new SecurityException("Deserialization error: " + e.getMessage());
        }
    }
}
