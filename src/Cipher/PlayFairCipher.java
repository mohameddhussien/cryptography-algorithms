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
		message = message.toLowerCase().replaceAll("[^a-z]", "").replaceAll("j", "i");

		int length = message.length();
		StringBuilder preparedMessage = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char current = message.charAt(i);
			preparedMessage.append(current);

			if (i >= length - 1)
				break;

			char next = message.charAt(i + 1);

			if (current != next) {
				preparedMessage.append(next);
				i++;
				continue;
			}

			preparedMessage.append('x');
			i--;
		}

		if (preparedMessage.length() % 2 != 0)
			preparedMessage.append('x');
		return preparedMessage.toString();
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
					matrix[posA[0]][(!reverse ? posA[1] + 1 : posA[1] + 4) % 5],
					matrix[posB[0]][(!reverse ? posB[1] + 1 : posB[1] + 4) % 5]
			});
		else if (posA[1] == posB[1])
			sb.append(new char[] {
					matrix[(!reverse ? posA[0] + 1 : posA[0] + 4) % 5][posA[1]],
					matrix[(!reverse ? posB[0] + 1 : posB[0] + 4) % 5][posB[1]]
			});
		else
			sb.append(new char[] {
					matrix[posA[0]][posB[1]], matrix[posB[0]][posA[1]]
			});

		return sb.toString();
	}

	@Override
	public String encrypt(String message) {
		this.generateMatrix(this.key);

		String preparedMessage = this.prepare(message);
		StringBuilder cipher = new StringBuilder();

		for (int i = 0; i < preparedMessage.length(); i += 2) {
			char current = preparedMessage.charAt(i);
			char next = preparedMessage.charAt(i + 1);

			cipher.append(this.substitute(current, next, false));
		}

		return cipher.toString();
	}

	@Override
	public String decrypt(String cipher) {
		this.generateMatrix(this.key);

		StringBuilder message = new StringBuilder();

		for (int i = 0; i < cipher.length(); i += 2) {
			char current = cipher.charAt(i);
			char next = cipher.charAt(i + 1);

			message.append(this.substitute(current, next, true));
		}

		return message.toString();
	}

}
