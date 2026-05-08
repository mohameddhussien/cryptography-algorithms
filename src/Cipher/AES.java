package Cipher;

public class AES extends Encryptor {
	// NOTE: Keeping the AES key hard-coded makes this class fixed to one key.
	// Hint: pass the key through the constructor or method input if you want a
	// reusable cipher.
	private final String key;
	private final int n;

	private static final int[] SBOX = {
			0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
			0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
			0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
			0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
			0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
			0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
			0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
			0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
			0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
			0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
			0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
			0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
			0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
			0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
			0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
			0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16,
	};

	private static final int[] RCONS = { 0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, };

	public AES(String key, int rounds) {
		this.key = key;
		this.n = rounds * 4 + 4;
	}

	private static void subBytes(int[][] state) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				state[i][j] = SBOX[state[i][j]];
	}

	private static void mixColumns(int[][] state) {
		for (int i = 0; i < 4; i++) {
			int s0 = state[0][i], s1 = state[1][i], s2 = state[2][i], s3 = state[3][i];
			state[0][i] = mul2(s0) ^ mul3(s1) ^ s2 ^ s3;
			state[1][i] = s0 ^ mul2(s1) ^ mul3(s2) ^ s3;
			state[2][i] = s0 ^ s1 ^ mul2(s2) ^ mul3(s3);
			state[3][i] = mul3(s0) ^ s1 ^ s2 ^ mul2(s3);
		}
	}

	private static int rotateWord(int x) {
		return (x << 8) | (x >>> 24);
	}

	private static int subWord(int x) {
		int b0 = SBOX[(x >>> 24) & 0xFF];
		int b1 = SBOX[(x >>> 16) & 0xFF];
		int b2 = SBOX[(x >>> 8) & 0xFF];
		int b3 = SBOX[x & 0xFF];

		return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
	}

	private static int mul2(int x) {
		int shifted = (x << 1) & 0xFF;
		return (x & 0x80) == 0 ? shifted : shifted ^ 0x1b;
	}

	private static int mul3(int x) {
		return mul2(x) ^ x;
	}

	private static void shiftRows(int[][] state) {
		int temp = state[1][0];
		state[1][0] = state[1][1];
		state[1][1] = state[1][2];
		state[1][2] = state[1][3];
		state[1][3] = temp;

		temp = state[2][0];
		int temp2 = state[2][1];
		state[2][0] = state[2][2];
		state[2][1] = state[2][3];
		state[2][2] = temp;
		state[2][3] = temp2;

		temp = state[3][3];
		state[3][3] = state[3][2];
		state[3][2] = state[3][1];
		state[3][1] = state[3][0];
		state[3][0] = temp;
	}

	private static int[][] createStateMatrix(String hex) {
		int[][] matrix = new int[4][4];
		// NOTE: This loads bytes in AES column-major order.
		for (int i = 0; i < 16; i++)
			matrix[i % 4][i / 4] = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16); // Flip rows and columns.
		return matrix;
	}

	private int[] createRoundKeys(String key) {
		int[] keys = new int[n];

		for (int i = 0; i < 4; i++)
			keys[i] = Integer.parseUnsignedInt(key.substring(i * 8, i * 8 + 8), 16);

		for (int i = 4; i < n; i++) {
			int prevWord = keys[i - 1];
			keys[i] = keys[i - 4] ^ (i % 4 == 0 ? g(prevWord, i) : prevWord);
		}

		return keys;
	}

	private static int g(int word, int i) {
		return subWord(rotateWord(word)) ^ (RCONS[i / 4] << 24);
	}

	private static void addRoundKey(int[][] state, int[][] roundKey) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				state[i][j] ^= roundKey[i][j];

	}

	private static int[][] getRoundKey(int[] roundKeys, int round) {
		int[][] roundKey = new int[4][4];

		for (int i = 0; i < 4; i++) {
			int word = roundKeys[round * 4 + i];
			roundKey[0][i] = (word >>> 24) & 0xFF;
			roundKey[1][i] = (word >>> 16) & 0xFF;
			roundKey[2][i] = (word >>> 8) & 0xFF;
			roundKey[3][i] = word & 0xFF;
		}
		return roundKey;
	}

	@Override
	public String encrypt(String message) {
		int[][] state = createStateMatrix(message);
		int[] roundKeys = createRoundKeys(this.key);

		addRoundKey(state, getRoundKey(roundKeys, 0));

		for (int round = 1; round <= 10; round++) {
			subBytes(state);
			shiftRows(state);

			if (round != 10)
				mixColumns(state);
			addRoundKey(state, getRoundKey(roundKeys, round));
		}

		return stateToHex(state);
	}

	private static String stateToHex(int[][] state) {
		StringBuilder str = new StringBuilder();
		for (int col = 0; col < 4; col++)
			for (int row = 0; row < 4; row++)
				str.append(String.format("%02x", state[row][col]));

		return str.toString();
	}

	@Override
	public String decrypt(String cipher) {
		throw new UnsupportedOperationException("Unimplemented method 'decrypt'");
	}

}
