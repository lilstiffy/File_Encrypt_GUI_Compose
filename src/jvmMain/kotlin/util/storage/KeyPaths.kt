package util.storage

import java.io.Serializable

data class AppSettings(
    var publicKeyPath: String? = null,
    var privateKeyPath: String? = null
): Serializable {
    companion object {
        const val FILE_NAME = "app_settings"
    }
}
