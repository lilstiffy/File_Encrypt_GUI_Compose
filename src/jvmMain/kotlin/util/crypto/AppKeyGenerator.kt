package util.crypto

import util.crypto.CryptoAlgorithm.AES
import util.crypto.CryptoAlgorithm.RSA
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

enum class CryptoAlgorithm(val value: String) {
    RSA("RSA"), AES("AES")
}

class AppKeyGenerator {
    companion object {
        val KEY_SIZES: List<Int> = listOf(1024, 2048, 3072, 4096)
    }
    /**
     * This code generates a symmetric encryption key using the Advanced Encryption Standard (AES) algorithm with a key size of [withSize] bits.
     * @author Peter Westin & Stefan Smudja
     */
    fun generateAsymetricKey(withSize: Int): SecretKeySpec {
        with(KeyGenerator.getInstance(AES.value)) {
            init(withSize)
            return SecretKeySpec(generateKey().encoded, AES.value)
        }
    }

    /**
     * This method generates a new public-private key pair for RSA encryption/decryption with a key size specified by [withSize] bits.
     * @author Peter Westin & Stefan Smudja
     */
    fun generateRSAKeyPair(withSize: Int): KeyPair {
        with(KeyPairGenerator.getInstance(RSA.value)) {
            initialize(withSize)
            return generateKeyPair()
        }
    }
}