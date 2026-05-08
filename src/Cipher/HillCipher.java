package Cipher;

public class HillCipher extends Encryptor {
	public final int dim = 3;
	private final int[][] key;

	public HillCipher(int[][] key) {
		this.key = key;
	}

	private String prepare(String message) {
		StringBuilder prepared = new StringBuilder(message.toLowerCase().replaceAll("[^a-z]", ""));
		while (prepared.length() % this.dim != 0)
			prepared.append('x');
		return prepared.toString();
	}

	@Override
	public String encrypt(String message) {
		String preparedMessage = this.prepare(message);
		StringBuilder cipher = new StringBuilder();
		for (int i = 0; i < preparedMessage.length(); i += this.dim) {
			int[] p = new int[this.dim], c = new int[this.dim];

			for (int j = 0; j < this.dim; j++)
				p[j] = preparedMessage.charAt(i + j) - 'a';

			for (int row = 0; row < this.dim; row++) {
				c[row] = 0;
				for (int col = 0; col < this.dim; col++)
					c[row] += this.key[row][col] * p[col];
				c[row] %= 26;
			}

			for (int col = 0; col < this.dim; col++)
				cipher.append((char) (c[col] + 'a'));
		}
		return cipher.toString();
	}

	@Override
	public String decrypt(String message) {
		String preparedMessage = this.prepare(message);
		int[][] inverseKey = new int[this.dim][this.dim];
		int[][] cofactors = new int[this.dim][this.dim];

		int a = this.key[0][0], b = this.key[0][1], c = this.key[0][2];
		int d = this.key[1][0], e = this.key[1][1], f = this.key[1][2];
		int g = this.key[2][0], h = this.key[2][1], i = this.key[2][2];

		int determinant = a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);
		determinant %= 26;
		if (determinant < 0)
			determinant += 26;

		int determinantInverse = -1;
		for (int value = 1; value < 26; value++) {
			if ((determinant * value) % 26 == 1) {
				determinantInverse = value;
				break;
			}
		}
		if (determinantInverse == -1)
			throw new IllegalArgumentException("Key matrix is not invertible modulo 26");

		cofactors[0][0] = e * i - f * h;
		cofactors[0][1] = -(d * i - f * g);
		cofactors[0][2] = d * h - e * g;
		cofactors[1][0] = -(b * i - c * h);
		cofactors[1][1] = a * i - c * g;
		cofactors[1][2] = -(a * h - b * g);
		cofactors[2][0] = b * f - c * e;
		cofactors[2][1] = -(a * f - c * d);
		cofactors[2][2] = a * e - b * d;

		for (int row = 0; row < this.dim; row++) {
			for (int col = 0; col < this.dim; col++) {
				int value = cofactors[col][row] * determinantInverse;
				value %= 26;
				if (value < 0)
					value += 26;
				inverseKey[row][col] = value;
			}
		}

		StringBuilder plain = new StringBuilder(preparedMessage.length());
		for (int block = 0; block < preparedMessage.length(); block += this.dim) {
			int p0 = preparedMessage.charAt(block) - 'a';
			int p1 = preparedMessage.charAt(block + 1) - 'a';
			int p2 = preparedMessage.charAt(block + 2) - 'a';

			int c0 = (inverseKey[0][0] * p0 + inverseKey[0][1] * p1 + inverseKey[0][2] * p2) % 26;
			int c1 = (inverseKey[1][0] * p0 + inverseKey[1][1] * p1 + inverseKey[1][2] * p2) % 26;
			int c2 = (inverseKey[2][0] * p0 + inverseKey[2][1] * p1 + inverseKey[2][2] * p2) % 26;

			plain.append((char) (c0 + 'a'));
			plain.append((char) (c1 + 'a'));
			plain.append((char) (c2 + 'a'));
		}
		return plain.toString();
	}

}
