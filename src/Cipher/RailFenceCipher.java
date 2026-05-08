package Cipher;

public class RailFenceCipher extends Encryptor {
	private int rows;

	public RailFenceCipher(int depth) {
		this.rows = depth;
	}

	@Override
	public String encrypt(String message) {
		String preparedMessage = message.toUpperCase().replaceAll("[^A-Z]", "");
		int cols = (int) Math.ceil((double) preparedMessage.length() / this.rows);
		char[][] matrix = new char[this.rows][cols];

		int index = 0;
		for (int j = 0; j < cols; j++) {
			for (int i = 0; i < this.rows; i++) {
				if (index < preparedMessage.length())
					matrix[i][j] = preparedMessage.charAt(index++);
				else
					matrix[i][j] = 'X';
			}
		}

		StringBuilder cipher = new StringBuilder();
		for (int i = 0; i < this.rows; i++)
			for (int j = 0; j < cols; j++)
				cipher.append(matrix[i][j]);

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		int cols = cipher.length() / this.rows;
		char[][] matrix = new char[this.rows][cols];

		int index = 0;
		for (int i = 0; i < this.rows; i++)
			for (int j = 0; j < cols; j++)
				matrix[i][j] = cipher.charAt(index++);

		StringBuilder message = new StringBuilder();
		for (int j = 0; j < cols; j++)
			for (int i = 0; i < this.rows; i++)
				message.append(matrix[i][j]);

		return message.toString();
	}
}
