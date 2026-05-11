package Cipher;

import java.util.List;

public class ColumnarTranspositionCipher extends Encryptor {
	private List<Integer> keyOrder;

	public ColumnarTranspositionCipher(List<Integer> key) {
		this.keyOrder = key;
	}

	@Override
	public String encrypt(String message) {
		int cols = this.keyOrder.size();
		int rows = (int) Math.ceil(message.length() / cols);

		char[][] matrix = new char[rows][cols];

		int index = 0;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				matrix[i][j] = index >= message.length() ? 'X' : message.charAt(index++);

		StringBuilder cipher = new StringBuilder();
		for (int k = 1; k <= cols; k++)
			for (int i = 0; i < rows; i++)
				cipher.append(matrix[i][keyOrder.indexOf(k)]);

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		int cols = this.keyOrder.size();
		int rows = cipher.length() / cols;

		char[][] matrix = new char[rows][cols];

		int index = 0;
		for (int k = 1; k <= cols; k++)
			for (int i = 0; i < rows; i++)
				matrix[i][keyOrder.indexOf(k)] = cipher.charAt(index++);

		StringBuilder msg = new StringBuilder();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				msg.append(matrix[i][j]);

		return msg.toString();
	}
}
