package Cipher;

import java.util.List;

public class ColumnarTranspositionCipher extends Encryptor {
	private List<Integer> keyOrder;

	public ColumnarTranspositionCipher(List<Integer> key) {
		this.keyOrder = key;
	}

	@Override
	public String encrypt(String message) {
		message = message.toUpperCase().replaceAll("[^A-Z]", "");
		int cols = this.keyOrder.size();
		int rows = (int) Math.ceil((double) message.length() / cols);
		char[][] matrix = new char[rows][cols];

		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (index < message.length())
					matrix[i][j] = message.charAt(index++);
				else
					matrix[i][j] = 'X';
			}
		}

		StringBuilder cipher = new StringBuilder();
		for (int j = 1; j <= cols; j++)
			for (int i = 0; i < rows; i++)
				cipher.append(matrix[i][this.keyOrder.indexOf(j)]);

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		int cols = this.keyOrder.size();
		int rows = cipher.length() / cols;
		char[][] matrix = new char[rows][cols];

		int index = 0;
		for (int j = 1; j <= cols; j++)
			for (int i = 0; i < rows; i++)
				matrix[i][this.keyOrder.indexOf(j)] = cipher.charAt(index++);

		StringBuilder message = new StringBuilder();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				message.append(matrix[i][j]);

		return message.toString();
	}
}
