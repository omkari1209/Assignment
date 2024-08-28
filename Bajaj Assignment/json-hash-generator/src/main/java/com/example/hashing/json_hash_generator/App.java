package com.example.hashing.json_hash_generator;

/**
 * Hello world!
 *
 */

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class App 
{
	 public static void main(String[] args) {
	        // Check if the correct number of arguments are provided
	        if (args.length != 2) {
	            System.err.println("Usage: java -jar test.jar <PRN Number> <path to JSON file>");
	            System.exit(1);
	        }

	        // Extract command-line arguments
	        String prnNumber = args[0].toLowerCase();
	        String jsonFilePath = args[1];

	        // Read and parse JSON file
	        String destinationValue = readDestinationValueFromJson(jsonFilePath);
	        if (destinationValue == null) {
	            System.err.println("Key 'destination' not found in the JSON file.");
	            System.exit(1);
	        }

	        // Generate a random 8-character alphanumeric string
	        String randomString = generateRandomString(8);

	        // Concatenate PRN Number, destination value, and random string
	        String concatenatedString = prnNumber + destinationValue + randomString;

	        // Generate MD5 hash
	        String md5Hash = generateMD5Hash(concatenatedString);

	        // Output result in the format: <hash>;<random string>
	        System.out.println(md5Hash + ";" + randomString);
	    }

	    private static String readDestinationValueFromJson(String filePath) {
	        try (FileInputStream fis = new FileInputStream(filePath)) {
	            JSONTokener tokener = new JSONTokener(fis);
	            JSONObject jsonObject = new JSONObject(tokener);

	            return findDestinationValue(jsonObject);
	        } catch (IOException e) {
	            System.err.println("Error reading JSON file: " + e.getMessage());
	            return null;
	        }
	    }

	    private static String findDestinationValue(JSONObject jsonObject) {
	        // Traverse JSON object to find the first instance of the key "destination"
	        return findDestinationValueHelper(jsonObject);
	    }

	    private static String findDestinationValueHelper(JSONObject jsonObject) {
	        for (String key : jsonObject.keySet()) {
	            if (key.equals("destination")) {
	                return jsonObject.getString(key);
	            } else {
	                Object value = jsonObject.get(key);
	                if (value instanceof JSONObject) {
	                    String result = findDestinationValueHelper((JSONObject) value);
	                    if (result != null) {
	                        return result;
	                    }
	                }
	            }
	        }
	        return null;
	    }

	    private static String generateRandomString(int length) {
	        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	        Random random = new Random();
	        StringBuilder sb = new StringBuilder(length);

	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(characters.length());
	            sb.append(characters.charAt(index));
	        }
	        return sb.toString();
	    }

	    private static String generateMD5Hash(String input) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            byte[] hashBytes = md.digest(input.getBytes());

	            StringBuilder sb = new StringBuilder();
	            for (byte b : hashBytes) {
	                sb.append(String.format("%02x", b));
	            }
	            return sb.toString();
	        } catch (NoSuchAlgorithmException e) {
	            System.err.println("Error generating MD5 hash: " + e.getMessage());
	            return null;
	        }
	    }
}
