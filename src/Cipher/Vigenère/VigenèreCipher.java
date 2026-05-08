package Cipher.Vigenère;

import Cipher.Encryptor;

public class VigenèreCipher extends Encryptor {
	private String key;
	private final VigenèreAlgorithm algorithm;

	public VigenèreCipher(String key, VigenèreAlgorithm algorithm) {
		this.key = key;
		this.algorithm = algorithm;
	}

	private String prepareMessage(String message) {
		return message.toLowerCase().replaceAll("[^a-z]", "");
	}

	private void prepareKey(String message, VigenèreAlgorithm algorithm) {
		this.key = this.key.toLowerCase().replaceAll("[^a-z]", "");

		if (algorithm == VigenèreAlgorithm.REPEATING_KEY && this.key.length() < message.length())
			this.key = this.key.repeat(message.length() / this.key.length() + 1).substring(0, message.length());
		if (algorithm == VigenèreAlgorithm.AUTO_KEY)
			this.key = new StringBuilder(this.key + message).substring(0, message.length());

		if (this.key.length() != message.length())
			throw new IllegalArgumentException("Invalid key length for one-time pad");
	}

	@Override
	public String encrypt(String message) {
		String preparedMessage = this.prepareMessage(message);
		this.prepareKey(preparedMessage, this.algorithm);

		StringBuilder cipher = new StringBuilder();
		for (int i = 0; i < preparedMessage.length(); i++) {
			int p = preparedMessage.charAt(i) - 'a', k = this.key.charAt(i) - 'a';
			cipher.append((char) ((p + k) % 26 + 'a'));
		}

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < cipher.length(); i++) {
			int c = cipher.charAt(i) - 'a', k = this.key.charAt(i) - 'a';
			message.append((char) ((c - k + 26) % 26 + 'a'));
		}
		return message.toString();
	}

}
