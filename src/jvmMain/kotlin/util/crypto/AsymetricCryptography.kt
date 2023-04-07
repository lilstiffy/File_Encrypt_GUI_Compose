package util.crypto

import util.crypto.CryptoAlgorithm.AES
import util.crypto.CryptoAlgorithm.RSA
import org.apache.commons.codec.binary.Base64
import java.io.InputStream
import java.io.OutputStream
import java.security.Key
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class AsymetricCryptography {

    companion object {
        private const val FILE_CRYPT_ALGORITHM = "AES/CF88/NoPadding"
        private const val IO_STREAM_BUFFER_SIZE = 1024 * 16
    }

    fun encryptIvBytes(publicKey: PublicKey, ivBytes: ByteArray): String? {
        return try {
            val encodedKey = Base64.encodeBase64String(ivBytes)
            val plainBytes = encodedKey.toByteArray() //Default UTF-8
            val cipher = getCipher(RSA, Cipher.ENCRYPT_MODE, publicKey)
            val encryptedBytes = cipher.doFinal(plainBytes)
            Base64.encodeBase64String(encryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decryptIvBytes(privateKey: PrivateKey, cipherText: String): IvParameterSpec? {
        return try {
            val plainBytes = Base64.decodeBase64(cipherText.toByteArray())
            val cipher = getCipher(RSA, Cipher.DECRYPT_MODE, privateKey)
            val decrypted = cipher.doFinal(plainBytes)
            IvParameterSpec(Base64.decodeBase64(String(decrypted)))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun getCipher(algorithm: CryptoAlgorithm, mode: Int, key: Key): Cipher {
        val cipher = Cipher.getInstance(algorithm.value)
        cipher.init(mode, key)
        return cipher
    }

    fun retrieveSymmetricKey(privateKey: PrivateKey, encryptedAesKey: String): SecretKeySpec? {
        return decryptSecretKey(privateKey, encryptedAesKey)
    }

    fun retrieveEncryptedSymmetricKey(publicKey: PublicKey, secretKeySpec: SecretKeySpec): String? {
        return encryptSecretKey(publicKey, secretKeySpec)
    }

    fun retrieveIvBytes(privateKey: PrivateKey, encryptedIvBytes: String): IvParameterSpec? {
        return decryptIvBytes(privateKey, encryptedIvBytes)
    }

    fun retrieveEncryptedIvBytes(publicKey: PublicKey?, ivBytes: ByteArray?): String? {
        return encryptIvBytes(publicKey!!, ivBytes!!)
    }

    private fun encryptSecretKey(pubKey: PublicKey, aesKeySpec: SecretKeySpec): String? {
        return try {
            val encodedKey = Base64.encodeBase64String(aesKeySpec.encoded)
            val plainBytes: ByteArray = encodedKey.toByteArray()
            val cipher = Cipher.getInstance(RSA.value)
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            val encrypted = cipher.doFinal(plainBytes)
            return String(Base64.encodeBase64(encrypted))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decryptSecretKey(privateKey: PrivateKey, cipherText: String): SecretKeySpec? {
        return try {
            val plainBytes: ByteArray = Base64.decodeBase64(cipherText.toByteArray())
            val cipher = Cipher.getInstance(RSA.value)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val decrypted = cipher.doFinal(plainBytes)
            val decodedKey = Base64.decodeBase64(String(decrypted))
            return SecretKeySpec(decodedKey, 0, decodedKey.size, AES.value)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun copyBytes(inputStream: InputStream, os: OutputStream) {
        var i: Int
        val b = ByteArray(8192)
        while (inputStream.read(b).also { i = it } != -1) {
            os.write(b, 0, i)
            os.flush()
        }
        os.close()
        inputStream.close()
    }
}