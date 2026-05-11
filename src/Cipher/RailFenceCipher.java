package Cipher;

public class RailFenceCipher extends Encryptor {
	private int depth;

	public RailFenceCipher(int depth) {
		this.depth = depth;
	}

	@Override
	public String encrypt(String message) {
		String preparedMessage = message.toLowerCase().replaceAll("[^a-z]", "");
		int cols = (int) Math.ceil((double) preparedMessage.length() / this.depth);
		char[][] matrix = new char[this.depth][cols];

		int index = 0;
		for (int j = 0; j < cols; j++)
			for (int i = 0; i < this.depth; i++)
				matrix[i][j] = index < preparedMessage.length() ? preparedMessage.charAt(index++) : 'x';

		StringBuilder cipher = new StringBuilder();
		for (int i = 0; i < this.depth; i++)
			for (int j = 0; j < cols; j++)
				cipher.append(matrix[i][j]);

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		int cols = cipher.length() / this.depth;
		char[][] matrix = new char[this.depth][cols];

		int index = 0;
		for (int i = 0; i < this.depth; i++)
			for (int j = 0; j < cols; j++)
				matrix[i][j] = cipher.charAt(index++);

		StringBuilder message = new StringBuilder();
		for (int j = 0; j < cols; j++)
			for (int i = 0; i < this.depth; i++)
				message.append(matrix[i][j]);

		return message.toString();
	}
}
