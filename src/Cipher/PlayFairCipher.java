package Cipher;

import java.util.stream.Collectors;

public class PlayFairCipher extends Encryptor {
	private final char[][] matrix = new char[5][5];
	private String key;

	public PlayFairCipher(String key) {
		this.key = key;
	}

	private void generateMatrix(String key) {
		String alphabet = "abcdefghiklmnopqrstuvwxyz";
		key.toLowerCase().replaceAll("[^a-z]", "").replaceAll("j", "i");
		StringBuilder combined = new StringBuilder(key + alphabet);
		String deduplicated = combined
				.chars()
				.distinct()
				.mapToObj(c -> String.valueOf((char) c))
				.collect(Collectors.joining());

		for (int i = 0; i < 25; i++)
			matrix[i / 5][i % 5] = deduplicated.charAt(i);
	}

	private String prepare(String message) {
		message = message.toLowerCase()
				.replaceAll("[^a-z]", "")
				.replace('j', 'i');

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < message.length(); i++) {
			char first = message.charAt(i);
			result.append(first);

			if (i + 1 >= message.length())
				continue;

			char second = message.charAt(i + 1);

			if (first == second)
				result.append('x');
			else {
				result.append(second);
				i++;
			}
		}

		if (result.length() % 2 != 0)
			result.append('x');

		return result.toString();
	}

	private int[] find(char a) {
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if (a == matrix[i][j])
					return new int[] { i, j };
		return null;
	}

	private String substitute(char a, char b, boolean reverse) {
		if (a == 'j')
			a = 'i';
		if (b == 'j')
			b = 'i';

		int[] posA = find(a), posB = find(b);
		StringBuilder sb = new StringBuilder();

		if (posA[0] == posB[0])
			sb.append(new char[] {
					matrix[posA[0]][(!reverse ? posA[1] + 1 : posA[1] - 1) % 5],
					matrix[posB[0]][(!reverse ? posB[1] + 1 : posB[1] - 1) % 5]
			});
		else if (posA[1] == posB[1])
			sb.append(new char[] {
					matrix[(!reverse ? posA[0] + 1 : posA[0] - 1) % 5][posA[1]],
					matrix[(!reverse ? posB[0] + 1 : posB[0] - 1) % 5][posB[1]]
			});
		else {
			sb.append(matrix[posA[0]][posB[1]]); // Row zai ma howa w el col byt8ayar
			sb.append(matrix[posB[0]][posA[1]]);
		}

		return sb.toString();
	}

	@Override
	public String encrypt(String message) {
		this.generateMatrix(this.key);

		String preparedMessage = this.prepare(message);
		StringBuilder cipher = new StringBuilder();

		for (int i = 0; i < preparedMessage.length(); i += 2)
			cipher.append(this.substitute(preparedMessage.charAt(i), preparedMessage.charAt(i + 1), false));

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		this.generateMatrix(this.key);

		StringBuilder message = new StringBuilder();

		for (int i = 0; i < cipher.length(); i += 2)
			message.append(this.substitute(cipher.charAt(i), cipher.charAt(i + 1), true));

		return message.toString();
	}

}
