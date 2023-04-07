package views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import theme.AppTheme
import util.crypto.AppKeyGenerator
import util.crypto.EncryptionHandler
import util.storage.AppSettings
import util.storage.FileHandler
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.security.PrivateKey
import java.security.PublicKey
import javax.swing.JFileChooser

@Preview
@Composable
fun FileEncryptorView(viewModel: FileEncryptorViewModel) {
    MaterialTheme {
        AppTheme.Background(Color.Black) {
            //Vertical StackView
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Title
                Text(
                    text = "File Encryptor Compose",
                    style = AppTheme.titleStyle(),
                    modifier = Modifier.padding(vertical = 32.dp)
                )

                Text(text = "Encryption", style = AppTheme.subTitleStyle())

                //Margin
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val dialog = FileDialog(Frame(), "Select file to encrypt")
                    dialog.mode = FileDialog.LOAD
                    dialog.isVisible = true
                    if (dialog.file != null) {
                        viewModel.encryptFile(File(dialog.directory + dialog.file))
                    }
                }) { Text("Encrypt") }
                Button(onClick = {
                    val dialog = FileDialog(Frame(), "Select file to decrypt")
                    dialog.mode = FileDialog.LOAD
                    dialog.isVisible = true
                    if (dialog.file != null) {
                        viewModel.decryptFile(File(dialog.directory + dialog.file))
                    }
                }) { Text("Decrypt") }

                Spacer(modifier = Modifier.height(16.dp))

                //Title
                Text(text = "Key handling", style = AppTheme.subTitleStyle())

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val dialog = FileDialog(Frame(), "Select a public key")
                    dialog.isVisible = true
                    if (dialog.file != null) {
                        viewModel.apply {
                            selectedPublicKey.value =
                                FileHandler.readFileToObject(File(dialog.directory + dialog.file))
                            selectedPublicKeyPath.value = dialog.directory + dialog.file
                            saveSettings()
                            println(selectedPublicKey.value)
                        }
                    }
                }) {
                    Text("Set Public key ${if (viewModel.selectedPublicKey.value != null) "✅" else "❌"}")
                }

                Button(onClick = {
                    val dialog = FileDialog(Frame(), "Select a private key")
                    dialog.isVisible = true
                    if (dialog.file != null) {
                        viewModel.apply {
                            selectedPrivateKey.value =
                                FileHandler.readFileToObject(File(dialog.directory + dialog.file))
                            selectedPrivateKeyPath.value = dialog.directory + dialog.file
                            saveSettings()
                            println(selectedPublicKey.value)
                        }
                    }
                }) {
                    Text("Set Private key ${if (viewModel.selectedPrivateKey.value != null) "✅" else "❌"}")
                }

                //Generate keypair button
                Button(onClick = {
                    with(JFileChooser()) {
                        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                        val result = showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION)
                            viewModel.generateKeypair(selectedFile.absolutePath)
                    }
                }) {
                    //Button Text
                    Text("Generate new key pair")
                }

                //Key size picker
                AppTheme.DropdownSelector(
                    values = AppKeyGenerator.KEY_SIZES,
                    suffix = "bits",
                    onValueSelected = { newVal ->
                        viewModel.selectedKeySize.value = newVal
                    }
                )

            }
        }
    }
}

class FileEncryptorViewModel {
    //States
    val selectedKeySize = mutableStateOf(4096)
    val selectedPrivateKey = mutableStateOf<PrivateKey?>(null)
    val selectedPrivateKeyPath = mutableStateOf<String?>(null)
    val selectedPublicKey = mutableStateOf<PublicKey?>(null)
    val selectedPublicKeyPath = mutableStateOf<String?>(null)

    init {
        val settings = FileHandler.readFileToObject<AppSettings>(File(AppSettings.FILE_NAME))
        settings?.let { unwrappedSettings ->
            selectedPublicKey.value = FileHandler.readFileToObject(unwrappedSettings.publicKeyPath?.let { File(it) })
            selectedPrivateKey.value = FileHandler.readFileToObject(unwrappedSettings.privateKeyPath?.let { File(it) })
            selectedPublicKeyPath.value = unwrappedSettings.publicKeyPath
            selectedPrivateKeyPath.value = unwrappedSettings.privateKeyPath
        }
    }

    fun saveSettings() {
        AppSettings(
            publicKeyPath = selectedPublicKeyPath.value,
            privateKeyPath = selectedPrivateKeyPath.value
        ).also {
            FileHandler.writeObjectToFile(it, File(System.getProperty("user.dir"), AppSettings.FILE_NAME))
        }
    }

    fun generateKeypair(path: String) {
        with(AppKeyGenerator()) {
            val keypair = generateRSAKeyPair(selectedKeySize.value)
            keypair.private.let { FileHandler.writeObjectToFile(it, File(path, "private_key")) }
            keypair.public.let { FileHandler.writeObjectToFile(it, File(path, "public_key")) }
        }
    }

    fun encryptFile(file: File) {
        with(EncryptionHandler()) {
            encryptFile(inputPath = file.absolutePath, publicKey = selectedPublicKey.value ?: return)
        }
    }

    fun decryptFile(file: File) {
        with(EncryptionHandler()) {
            decryptFile(inputFile = file.absoluteFile, privateKey = selectedPrivateKey.value ?: return)
        }
    }
}