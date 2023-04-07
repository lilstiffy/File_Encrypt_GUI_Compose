
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import views.FileEncryptorView
import views.FileEncryptorViewModel


fun main() = application {
    Window(
        title = "File encryptor",
        resizable = false,
        onCloseRequest = ::exitApplication
    ) {
        FileEncryptorView(FileEncryptorViewModel())
    }
}