package Hash;

public class MD5 extends Hash {

	// A = 0x67452301, B = 0xEFCDAB89, C = 0x98BADCFE, D = 0x10325476.
	private int A0 = 0x01234567, B0 = 0x89ABCDEF, C0 = 0xFEDCBA98, D0 = 0x76543210;

	public static final int[][] ROUND_SHIFTS = {
			{ 7, 12, 17, 22 }, // Round 1
			{ 5, 9, 14, 20 }, // Round 2
			{ 4, 11, 16, 23 }, // Round 3
			{ 6, 10, 15, 21 } // Round 4
	};

	// Flattened array for easy access in a 64-iteration loop:
	// shift = SHIFTS_FLAT[i] where i is 0-63
	public static final int[] SHIFTS_FLAT = {
			7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, // Round 1
			5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, // Round 2
			4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, // Round 3
			6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 // Round 4
	};

	// T[i] = floor(2^32 * abs(sin(i+1)))
	// Defined as a static final int array for efficiency
	public static final int[] T = new int[64];

	static {
		for (int i = 0; i < 64; i++) {
			T[i] = (int) (long) Math.floor((1L << 32) * Math.abs(Math.sin(i + 1)));
		}
	}

	private int CLF(int n, int shift) {
		return (n << shift) | (n >>> (32 - shift));
	}

	private static int NonLinearFunction(int b, int c, int d, int round) {
		switch (round) {
			case 1:
				return b & c | ~b & d;

			case 2:
				return b & d | c & ~d;

			case 3:
				return b ^ c ^ d;
			case 4:
				return c ^ (b | ~d);
			default:
				throw new IllegalArgumentException("Invalid round in ... should be 1-4");
		}

	}

	private static int MessageIndex(int index, int round) {
		switch (round) {
			case 1:
				return index;
			case 2:
				return (1 + 5 * index) % 16;

			case 3:
				return (5 + 3 * index) % 16;

			case 4:
				return (7 * index) % 16;

			default:
				throw new IllegalArgumentException("Invalid round in ... should be 1-4");
		}
	}

	private int[] Round(int round, int[] M, int[] R) {
		int a = R[0], b = R[1], c = R[2], d = R[3];
		for (int i = 0; i < 16; i++) {
			int nlfPlusA = NonLinearFunction(b, c, d, round) + a;
			a = d;
			d = c;
			c = b;
			b = b + CLF(nlfPlusA + T[i + 16 * (round - 1)] + M[MessageIndex(i, round)],
					// ROUND_SHIFTS[round - 1][i % 4]
					SHIFTS_FLAT[i + 16 * (round - 1)]);
		}

		return new int[] { a, b, c, d };
	}

	private int Swap(int n) {
		return ((n << 24) & 0xFF000000) | ((n << 8) & 0x00FF0000) | ((n >>> 8) & 0x0000FF00) | (n >>> 24);
	}

	@Override
	public String encrypt(String message) {
		byte[] messageBytes = message.getBytes();
		long msgBits = messageBytes.length * 8, remainingBits = msgBits % 512;
		long paddingBytes = remainingBits < 448 ? (448 - remainingBits) / 8 : (512 - remainingBits + 448) / 8;

		byte[] paddedMessageBytes = new byte[messageBytes.length + (int) paddingBytes + 8];
		System.arraycopy(messageBytes, 0, paddedMessageBytes, 0, messageBytes.length);
		paddedMessageBytes[messageBytes.length] = (byte) 0x80;

		for (int i = 0; i < 8; i++)
			paddedMessageBytes[paddedMessageBytes.length + i - 8] = (byte) (msgBits >>> (i * 8));

		int A = A0, B = B0, C = C0, D = D0;

		for (int offset = 0; offset < paddedMessageBytes.length; offset += 64) { // For each 512 bit.
			int M[] = new int[16];
			for (int i = 0; i < 16; i++)
				// BUG: MD5 message words are little-endian. This currently builds each
				// word as big-endian. Unless your course explicitly changed this too,
				// reverse the shifts: byte0 + byte1<<8 + byte2<<16 + byte3<<24.
				M[i] = (((paddedMessageBytes[offset + i * 4] & 0xFF) << 24) |
						((paddedMessageBytes[offset + i * 4 + 1] & 0xFF) << 16) |
						((paddedMessageBytes[offset + i * 4 + 2] & 0xFF) << 8) |
						(paddedMessageBytes[offset + i * 4 + 3] & 0xFF));

			int[] registers = new int[] { A, B, C, D };

			for (int round = 1; round <= 4; round++)
				registers = Round(round, M, registers);

			A += registers[0];
			B += registers[1];
			C += registers[2];
			D += registers[3];
		}

		return String.format("%08x%08x%08x%08x", Swap(A), Swap(B), Swap(C), Swap(D));
	}
}
