import java.util.List;

import Cipher.*;
import Cipher.Vigenère.*;

public class App {
	public static void main(String[] args) throws Exception {
		testCipher(new CaesarCipher(1), "Mohamed Hussien");
		testCipher(
				new MonoalphabeticCipher("phqgiumeaylnofdxkrcvstzwbjPHQGIUMEAYLNOFDXKRCVSTZWBJ"),
				"Mohamed Hussien");
		testCipher(
				new RailFenceCipher(2), "Computer Science");
		testCipher(
				new ColumnarTranspositionCipher(List.of(1, 3, 4, 2, 5)), "Computer Science");
		testCipher(
				new PlayFairCipher("ballon"), "Computer Systems");
		testCipher(
				new HillCipher(new int[][] { { 17, 17, 5 }, { 21, 18, 21 }, { 2, 2, 19 } }),
				"pay more money");
		testCipher(
				new VigenèreCipher("hello", VigenèreAlgorithm.AUTO_KEY), "Computer");
		testCipher(
				new VigenèreCipher("hello", VigenèreAlgorithm.REPEATING_KEY), "Computer");
		testCipher(
				new VigenèreCipher("hello", VigenèreAlgorithm.ONE_TIME_PAD), "Computer");
		testCipher(
				new VigenèreCipher("hilmotry", VigenèreAlgorithm.ONE_TIME_PAD), "Computer");
		testCipher(new DES("133457799BBCDFF1"), "0123456789ABCDEF");
		testCipher(new AES("2b7e151628aed2a6abf7158809cf4f3c", 10), "32f3f6a8885a308d313198a2e0370734");
	}

	public static void testCipher(Encryptor cipher, String message) {
		try {
			System.out.println(cipher.getClass().getName());
			System.out.print("Original: " + message + " | ");
			String encryptedMessage = cipher.encrypt(message);
			System.out.print("Encrypted: " + encryptedMessage + " | ");
			String decryptedMessage = cipher.decrypt(encryptedMessage);
			System.out.println("Decrypted: " + decryptedMessage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("-------------------------------------");
		}

	}
}
