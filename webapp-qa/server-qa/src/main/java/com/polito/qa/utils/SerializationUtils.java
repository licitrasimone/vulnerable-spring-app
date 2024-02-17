package com.polito.qa.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SerializationUtils {
	public static String serialize(Object item) {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(item);
			objectOutputStream.close();
			
			byte[] bytes = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
			return new String(bytes, StandardCharsets.US_ASCII);
			
		}catch(IOException e) {
			throw new Error(e);
		}
	}
	
	public static Object deserialize(String data) {
		try {
			byte[] bytes = Base64.getDecoder().decode(data);
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			final Object obj = objectInputStream.readObject();
			objectInputStream.close();
			return obj;
		}catch(IOException e) {
			throw new Error(e);
		}catch(ClassNotFoundException e) {
			throw new Error(e);
		}
	}
}
