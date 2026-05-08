package Cipher;

public class CaesarCipher extends Encryptor {
	private int key;

	public CaesarCipher(int key) {
		this.key = key;
	}

	private String transform(String message, int key) {
		StringBuilder sb = new StringBuilder();
		for (char c : message.toCharArray()) {
			if (!Character.isLetter(c)) {
				sb.append(c);
				continue;
			}
			char base = Character.isLowerCase(c) ? 'a' : 'A';
			char encrypted = (char) ((c - base + key) % 26 + base);
			sb.append(encrypted);
		}
		return sb.toString();
	}

	@Override
	public String encrypt(String message) {
		return transform(message, key);
	}

	@Override
	public String decrypt(String message) {
		return transform(message, 26 - (this.key % 26));
	}
}
