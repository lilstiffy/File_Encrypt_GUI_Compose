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
import util.storage.FileHandler
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.security.Key
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
                verticalArrangement = Arrangement.Center
            ) {
                //Title
                Text(
                    text = "File Encryptor Compose",
                    style = AppTheme.titleStyle(),
                    modifier = Modifier.padding(vertical = 32.dp)
                )
                //Horizontal StackView
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    //Left Column
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Encryption", style = AppTheme.subTitleStyle())

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {

                        }) { Text("Encrypt") }
                        Button(onClick = {

                        }) { Text("Decrypt") }
                    }

                    //Right Column
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Title
                        Text(text = "Key handling", style = AppTheme.subTitleStyle())

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            val dialog = FileDialog(Frame(), "Select a file")
                            dialog.isVisible = true
                            if (dialog.file != null) {
                                viewModel.selectedPublicKey.value =
                                    FileHandler.readFileToObject<PublicKey>(File(dialog.directory + dialog.file))
                                println(viewModel.selectedPublicKey.value)
                            }
                        }) {
                            Text("Set Public key")
                        }

                        Button(onClick = {
                            val dialog = FileDialog(Frame(), "Select a file")
                            dialog.isVisible = true
                            if (dialog.file != null) {
                                viewModel.selectedPrivateKey.value =
                                    FileHandler.readFileToObject<PublicKey>(File(dialog.directory + dialog.file))                            }
                        }) {
                            Text("Set Private key")
                        }

                        //Generate keypair button
                        Button(onClick = {
                            with(JFileChooser()) {
                                fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                                val result = showOpenDialog(null)
                                if (result == JFileChooser.APPROVE_OPTION)
                                    viewModel.generateKeypair(selectedFile)
                            }
                        }) {
                            //Button Text
                            Text("Generate new key pair")
                        }
                    }
                }
            }
        }
    }
}

class FileEncryptorViewModel {
    //States
    val selectedFile = mutableStateOf<File?>(null)
    val selectedPrivateKey = mutableStateOf<Key?>(null)
    val selectedPublicKey = mutableStateOf<Key?>(null)

    fun generateKeypair(at: File) {
        with(AppKeyGenerator()) {
            val keypair = generateRSAKeyPair(4096)
            keypair.private.let { FileHandler.writeObjectToFile(it, "private_key", at.path) }
            keypair.public.let { FileHandler.writeObjectToFile(it, "public_key", at.path) }
        }
    }
}