package Cipher;

public class Helpers {

	public static long modPow(long B, long P, long N) { // B = Base, P = Power, N = Mod
		long mul = 1;

		for (long i = 0; i < P; i++)
			mul = (mul * (B % N)) % N;

		return mul;
	}

	public static long calculateInverse(long b, long n) {
		long Q, A1 = 1, A2 = 0, A3 = n, B1 = 0, B2 = 1, B3 = b;

		while (B3 != 0 && B3 != 1) {
			Q = A3 / B3;
			long _A1 = A1, _A2 = A2, _A3 = A3;
			A1 = B1;
			A2 = B2;
			A3 = B3;

			B1 = _A1 - B1 * Q;
			B2 = _A2 - B2 * Q;
			B3 = _A3 % B3;
		}

		if (B3 != 1)
			throw new IllegalArgumentException(String.format("b (%d) and n (%d) are not relatively prime", b, n));

		return B2;
	}
}
