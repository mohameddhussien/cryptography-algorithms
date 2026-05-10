package Cipher;

public class ElGamal {

	private long[] getKeys(long a, long q, long Xa, long Xb) {
		long Ya = Helpers.modPow(a, Xa, q);
		long Yb = Helpers.modPow(a, Xb, q);
		long S1 = Helpers.modPow(Yb, Xa, q);
		long S2 = Helpers.modPow(Ya, Xb, q);

		return new long[] { S1, S2 };
	}

	private long[] encrypt(long m, long q, long a, long k, long Yb) {
		long C1 = Helpers.modPow(a, k, q);
		long K = Helpers.modPow(Yb, k, q);
		long C2 = (m * K) % q;

		return new long[] { C1, C2 };
	}

	private long[] decrypt(long C2, long q, long a, long k, long C1) {
		long Yb = Helpers.modPow(a, k, q);
		long K = Helpers.modPow(C1, k, q);
		long m = (C2 * Helpers.calculateInverse(K, q)) % q;

		return new long[] { Yb, m };
	}
}
