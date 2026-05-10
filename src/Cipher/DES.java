package Cipher;

public class DES extends Encryptor {
	private final String key;
	private final int rounds = 16;
	// DES S-Boxes: 8 boxes, each is a 4x16 matrix
	public static final int[][][] S_BOX = {
			// S1
			{
					{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
					{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
					{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
					{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
			},
			// S2
			{
					{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
					{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
					{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
					{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
			},
			// S3
			{
					{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
					{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
					{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
					{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
			},
			// S4
			{
					{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
					{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
					{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
					{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
			},
			// S5
			{
					{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
					{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
					{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
					{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
			},
			// S6
			{
					{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
					{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
					{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
					{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
			},
			// S7
			{
					{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
					{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
					{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
					{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
			},
			// S8
			{
					{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
					{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
					{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
					{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
			}
	};

	// Expansion Table (E) - 32 bits to 48 bits
	public static final int[] E = {
			32, 1, 2, 3, 4, 5,
			4, 5, 6, 7, 8, 9,
			8, 9, 10, 11, 12, 13,
			12, 13, 14, 15, 16, 17,
			16, 17, 18, 19, 20, 21,
			20, 21, 22, 23, 24, 25,
			24, 25, 26, 27, 28, 29,
			28, 29, 30, 31, 32, 1
	};

	// Permutation Table (P) - 32 bits to 32 bits
	public static final int[] P = {
			16, 7, 20, 21,
			29, 12, 28, 17,
			1, 15, 23, 26,
			5, 18, 31, 10,
			2, 8, 24, 14,
			32, 27, 3, 9,
			19, 13, 30, 6,
			22, 11, 4, 25
	};

	// Initial Permutation (IP) - 64 bits to 64 bits
	public static final int[] IP = {
			58, 50, 42, 34, 26, 18, 10, 2,
			60, 52, 44, 36, 28, 20, 12, 4,
			62, 54, 46, 38, 30, 22, 14, 6,
			64, 56, 48, 40, 32, 24, 16, 8,
			57, 49, 41, 33, 25, 17, 9, 1,
			59, 51, 43, 35, 27, 19, 11, 3,
			61, 53, 45, 37, 29, 21, 13, 5,
			63, 55, 47, 39, 31, 23, 15, 7
	};

	// Final Permutation (FP) or Inverse Initial Permutation (IP-1)
	public static final int[] FP = {
			40, 8, 48, 16, 56, 24, 64, 32,
			39, 7, 47, 15, 55, 23, 63, 31,
			38, 6, 46, 14, 54, 22, 62, 30,
			37, 5, 45, 13, 53, 21, 61, 29,
			36, 4, 44, 12, 52, 20, 60, 28,
			35, 3, 43, 11, 51, 19, 59, 27,
			34, 2, 42, 10, 50, 18, 58, 26,
			33, 1, 41, 9, 49, 17, 57, 25
	};

	// Permuted Choice 1 (PC1) - 64-bit key to 56 bits
	public static final int[] PC1 = {
			57, 49, 41, 33, 25, 17, 9,
			1, 58, 50, 42, 34, 26, 18,
			10, 2, 59, 51, 43, 35, 27,
			19, 11, 3, 60, 52, 44, 36,
			63, 55, 47, 39, 31, 23, 15,
			7, 62, 54, 46, 38, 30, 22,
			14, 6, 61, 53, 45, 37, 29,
			21, 13, 5, 28, 20, 12, 4
	};

	// Permuted Choice 2 (PC2) - 56 bits to 48-bit subkey
	public static final int[] PC2 = {
			14, 17, 11, 24, 1, 5,
			3, 28, 15, 6, 21, 10,
			23, 19, 12, 4, 26, 8,
			16, 7, 27, 20, 13, 2,
			41, 52, 31, 37, 47, 55,
			30, 40, 51, 45, 33, 48,
			44, 49, 39, 56, 34, 53,
			46, 42, 50, 36, 29, 32
	};

	// Key Schedule Iteration Shifts
	public static final int[] SHIFTS = {
			1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	};

	public DES(String key) {
		this.key = key;
	}

	private static String leftShift(String input, int shift) {
		return input.substring(shift) + input.substring(0, shift);
	}

	private static String applyPermutation(String input, int[] permutation) {
		StringBuilder output = new StringBuilder(input.length());
		for (int pos : permutation)
			output.append(input.charAt(pos - 1));
		return output.toString();
	}

	private static String[] generateSubKeys(String key64) {
		String[] subKeys = new String[16];
		String key56 = applyPermutation(key64, PC1), C = key56.substring(0, 56 / 2), D = key56.substring(56 / 2);

		for (int i = 0; i < 16; i++) {
			C = leftShift(C, SHIFTS[i]);
			D = leftShift(D, SHIFTS[i]);
			subKeys[i] = applyPermutation(C + D, PC2);
		}

		return subKeys;
	}

	private static String xor(String a, String b) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < a.length(); i++)
			output.append((a.charAt(i) == b.charAt(i)) ? '0' : '1');
		return output.toString();
	}

	private static String hexToBin(String hex) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i++) {
			String c = Integer.toBinaryString(Integer.parseInt(String.valueOf(hex.charAt(i)), 16));
			while (c.length() < 4)
				c = "0" + c;
			output.append(c);
		}
		return output.toString();
	}

	private static String binToHex(String bin) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < bin.length(); i += 4)
			output.append(Integer.toHexString(Integer.parseInt(bin.substring(i, i + 4), 2)));
		return output.toString();
	}

	private static String f(String R, String K) {
		String xored = xor(applyPermutation(R, E), K);
		StringBuilder substituted = new StringBuilder();

		for (int i = 0; i < S_BOX.length; i++) {
			String block = xored.substring(i * 6, (i + 1) * 6);
			int row = Integer.parseInt("" + block.charAt(0) + block.charAt(5), 2);
			int col = Integer.parseInt(block.substring(1, 5), 2);

			String val = Integer.toBinaryString(S_BOX[i][row][col]);
			while (val.length() < 4)
				val = "0" + val;
			substituted.append(val);
		}

		return applyPermutation(substituted.toString(), P);
	}

	private String logic(String message, Boolean decrypt) {
		String binText = hexToBin(message);
		String[] keys = generateSubKeys(hexToBin(key));

		String init = applyPermutation(binText, IP), left = init.substring(0, 32), right = init.substring(32);

		for (int i = 0; i < rounds; i++) {
			String tmp = right;
			right = xor(left, f(right, keys[decrypt ? (rounds - i - 1) : i]));
			left = tmp;
		}

		return binToHex(applyPermutation(right + left, FP));
	}

	@Override
	public String encrypt(String message) {
		return this.logic(message, false);
	}

	@Override
	public String decrypt(String cipher) {
		return this.logic(cipher, true);
	}

}
