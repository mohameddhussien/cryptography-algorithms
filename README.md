# Cyber Security Algorithms in Java

## Project Summary

This repository is an educational Java project for implementing classical ciphers, modern block ciphers, public-key cryptography algorithms, and hashing methods from scratch. The code is organized around a simple encryption/decryption abstraction for ciphers and a separate hashing abstraction for one-way digest functions.

The project is useful for studying how cryptographic transformations work internally: substitution, transposition, modular arithmetic, matrix-based encryption, Feistel networks, block cipher rounds, public-key exponentiation, and message digest processing.

> Note: These implementations are for learning and experimentation. They should not be used as production security primitives.

## Metadata

| Field | Value |
| --- | --- |
| Language | Java |
| Project type | Console / source-code study project |
| Main entry point | `src/App.java` |
| Cipher package | `src/Cipher` |
| Hash package | `src/Hash` |
| Build output folder | `bin` |
| Dependency folder | `lib` |
| Primary abstraction | `Cipher.Encryptor` |
| Hash abstraction | `Hash.Hash` |

## Folder Structure

```text
src/
  App.java
  Cipher/
    AES.java
    CaesarCipher.java
    ColumnarTranspositionCipher.java
    DES.java
    ElGamal.java
    Encryptor.java
    Helpers.java
    HillCipher.java
    MonoalphabeticCipher.java
    PlayFairCipher.java
    RailFenceCipher.java
    RSA.java
    Vigenere/  (accented spelling is used in the source tree)
      VigenereAlgorithm.java
      VigenereCipher.java
  Hash/
    Hash.java
    MD5.java
```

## Core Interfaces

### `Encryptor`

`Cipher.Encryptor` is the base class for reversible cipher implementations.

```java
public abstract class Encryptor {
    public abstract String encrypt(String message);
    public abstract String decrypt(String cipher);
}
```

### `Hash`

`Hash.Hash` is the base class for one-way hash implementations.

```java
public abstract class Hash {
    public abstract String encrypt(String message);
}
```

## Classical Cipher Algorithms

### Caesar Cipher

A monoalphabetic substitution cipher that shifts each alphabetic character by a fixed integer key. The implementation preserves non-letter characters and supports both uppercase and lowercase English letters.

### Monoalphabetic Substitution Cipher

Maps every character in the English alphabet to a configured substitution alphabet. The implementation includes mappings for lowercase and uppercase letters, while leaving unmapped characters unchanged.

### Rail Fence Cipher

A transposition cipher that writes prepared text into a matrix by depth and then reads row by row. The implementation normalizes input to lowercase letters and pads incomplete cells with `x`.

### Columnar Transposition Cipher

Reorders message characters by writing them into a grid and reading columns according to a numeric key order. Padding is added with `X` when the final row is incomplete.

### Playfair Cipher

A digraph substitution cipher using a 5x5 key matrix. The implementation normalizes text to lowercase, merges `j` with `i`, inserts filler `x` between repeated letters, and encrypts/decrypts two-character blocks.

### Hill Cipher

A polygraphic substitution cipher based on matrix multiplication modulo 26. This project implements a 3x3 key matrix, pads plaintext to block size with `x`, and computes the inverse matrix for decryption.

### Vigenere Cipher

A polyalphabetic substitution cipher implemented with three keying modes:

- `REPEATING_KEY`
- `AUTO_KEY`
- `ONE_TIME_PAD`

The implementation prepares alphabetic lowercase text and derives the working key based on the selected `VigenereAlgorithm`.

Current status: the keying modes are represented in code, but the key preparation flow should be reviewed before treating the implementation as complete.

## Advanced and Modern Cipher Algorithms

### DES

Implements the Data Encryption Standard using a 16-round Feistel network. The class includes DES permutation tables, expansion, S-box substitution, P-box permutation, key schedule generation, and reversed subkeys for decryption.

Expected input format: hexadecimal 64-bit block and hexadecimal 64-bit key.

### AES

Implements core AES encryption operations for a 128-bit block, including state creation, SubBytes, ShiftRows, MixColumns, AddRoundKey, Rcon values, and round-key generation.

Current status: encryption is implemented. Decryption currently throws `UnsupportedOperationException`.

Expected input format: hexadecimal 128-bit block and hexadecimal 128-bit key.

### RSA

Implements a simple RSA workflow using two primes `p` and `q`, a public exponent `e`, modular exponentiation, Euler totient calculation, and modular inverse calculation for the private exponent.

Expected input format: numeric plaintext and ciphertext strings.

### ElGamal

Contains helper routines for ElGamal-style key agreement, encryption, and decryption using modular exponentiation and modular inverse arithmetic.

Current status: the class contains internal arithmetic methods and does not currently extend `Encryptor`.

## Hash Algorithms

### MD5

Implements an MD5-style message digest workflow with padding, 512-bit block processing, round constants, nonlinear functions, circular left shifts, message index selection, and four working registers.

Current status: the implementation is educational and should be validated against known MD5 test vectors before relying on the output for compatibility checks.

## Utility Methods

### `Helpers.modPow`

Computes modular exponentiation and is used by public-key algorithms such as RSA and ElGamal.

### `Helpers.calculateInverse`

Computes a modular multiplicative inverse using extended Euclidean-style state updates. It is used by Hill cipher decryption, RSA private exponent calculation, and ElGamal decryption.

## Running Examples

The `src/App.java` file contains commented examples for most algorithms. Uncomment one example at a time to test a cipher or hash method.

Example:

```java
testCipher(new CaesarCipher(1), "Mohamed Hussien");
testHash(new MD5(), "cryptography");
```

Compile and run from the project root with a Java setup such as VS Code's Java extension, or with command-line Java tooling configured for the `src` folder.

## Implementation Status

| Algorithm | Type | Encrypt | Decrypt | Notes |
| --- | --- | --- | --- | --- |
| Caesar Cipher | Classical substitution | Yes | Yes | Preserves non-letter characters |
| Monoalphabetic Cipher | Classical substitution | Yes | Yes | Uses provided substitution alphabet |
| Rail Fence Cipher | Classical transposition | Yes | Yes | Normalizes and pads text |
| Columnar Transposition Cipher | Classical transposition | Yes | Yes | Uses numeric key order |
| Playfair Cipher | Classical digraph substitution | Yes | Yes | Uses 5x5 matrix |
| Hill Cipher | Classical matrix cipher | Yes | Yes | Fixed 3x3 key matrix |
| Vigenere Cipher | Classical polyalphabetic cipher | Partial | Partial | Modes are defined; key preparation needs review |
| DES | Block cipher | Yes | Yes | 16-round Feistel implementation |
| AES | Block cipher | Yes | No | Encryption implemented; decryption pending |
| RSA | Public-key cipher | Yes | Yes | Numeric message blocks |
| ElGamal | Public-key arithmetic | Partial | Partial | Internal helper methods only |
| MD5 | Hash function | Yes | Not applicable | Educational digest implementation |

## Educational Scope

This codebase focuses on clarity and algorithmic practice rather than hardened cryptographic engineering. It is intended for coursework, experimentation, and comparing how different cipher families transform data.
