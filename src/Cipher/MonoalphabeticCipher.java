package Cipher;

import java.util.HashMap;

public class MonoalphabeticCipher extends Encryptor {
	private String key;

	public MonoalphabeticCipher(String key) {
		this.key = key;
	}

	private String transform(String message, String from, String to) {
		HashMap<Character, Character> map = new HashMap<>();

		for (int i = 0; i < from.length(); i++)
			map.put(from.charAt(i), to.charAt(i));

		StringBuilder res = new StringBuilder();
		for (char c : message.toCharArray())
			res.append(map.getOrDefault(c, c));

		return res.toString();
	}

	public String encrypt(String message) {
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return transform(message, alphabet, this.key);
	}

	public String decrypt(String message) {
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return transform(message, this.key, alphabet);
	}
}
