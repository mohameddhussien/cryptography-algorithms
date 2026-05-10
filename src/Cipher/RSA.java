package Cipher;

public class RSA extends Encryptor {

	private final long e, n, totient;

	public RSA(long p, long q, long e) {
		this.e = e;
		this.n = p * q;
		this.totient = (p - 1) * (q - 1);
	}

	@Override
	public String encrypt(String message) {
		long M = Long.parseLong(message);
		long C = Helpers.modPow(M, e, n);

		return String.valueOf(C);
	}

	@Override
	public String decrypt(String cipher) {
		long C = Long.parseLong(cipher);
		long M = Helpers.modPow(C, Helpers.calculateInverse(e, totient), n);

		return String.valueOf(M);
	}

}
