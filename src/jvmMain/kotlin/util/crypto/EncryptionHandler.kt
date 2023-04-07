package util.crypto

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import javax.crypto.Cipher

class EncryptionHandler {
    fun encryptFile(inputPath: String, publicKey: PublicKey) {
        val inputFile = File(inputPath)
        val outputFile = File("${inputFile.parent}/${inputFile.name}.enc")
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        Files.newInputStream(inputFile.toPath()).use { input ->
            BufferedOutputStream(FileOutputStream(outputFile)).use { output ->
                val keySizeBytes = (publicKey as RSAPublicKey).modulus.bitLength() / 8
                val buf = ByteArray(keySizeBytes - 11)
                var bytesRead: Int
                while (input.read(buf).also { bytesRead = it } != -1) {
                    val outputBytes = cipher.update(buf, 0, bytesRead)
                    output.write(outputBytes)
                }
                val outputBytes = cipher.doFinal()
                output.write(outputBytes)
            }
        }
        println("File encrypted successfully!")
    }

    fun decryptFile(inputFile: File, privateKey: PrivateKey) {
        val outputFile = if (inputFile.name.endsWith(".enc")) {
            File(inputFile.parent, inputFile.nameWithoutExtension)
        } else {
            File(inputFile.parent, "decrypted_${inputFile.name}")
        }

        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val keySizeBytes = (privateKey as RSAPrivateKey).modulus.bitLength() / 8
        val buf = ByteArray(keySizeBytes)

        inputFile.inputStream().use { input ->
            outputFile.outputStream().use { output ->
                var bytesRead: Int
                while (input.read(buf).also { bytesRead = it } != -1) {
                    val chunk = cipher.update(buf, 0, bytesRead)
                    if (chunk.isNotEmpty()) {
                        output.write(chunk)
                    }
                }
                val chunk = cipher.doFinal()
                if (chunk.isNotEmpty()) {
                    output.write(chunk)
                }
            }
        }
        println("File decrypted successfully!")
    }
}