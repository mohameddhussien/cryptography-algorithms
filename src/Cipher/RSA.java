package Cipher;

import java.math.BigInteger;

public class RSA extends Encryptor {

	private final long e, n, totient;

	public RSA(long p, long q, long e) {
		this.e = e;
		this.n = p * q;
		this.totient = (p - 1) * (q - 1);
	}

	private long calculateInverse(long b, long n) {
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

	@Override
	public String encrypt(String message) {
		long msg = Long.parseLong(message);
		long C = BigInteger.valueOf(msg).modPow(BigInteger.valueOf(e), BigInteger.valueOf(n)).longValue();

		return String.valueOf(C);
	}

	@Override
	public String decrypt(String cipher) {
		long C = Long.parseLong(cipher);
		long M = BigInteger.valueOf(C).modPow(BigInteger.valueOf(calculateInverse(e, totient)), BigInteger.valueOf(n))
				.longValue();

		return String.valueOf(M);
	}

}
