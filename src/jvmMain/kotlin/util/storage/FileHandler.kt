package util.storage

import java.io.*

object FileHandler {
    fun <T : Serializable> writeObjectToFile(obj: T, fileName: String, path: String) {
        try {
            ObjectOutputStream(FileOutputStream("$path/${fileName}.ser")).use { stream ->
                stream.writeObject(obj)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inline fun <reified T : Serializable> readFileToObject(file: File): T? {
        return try {
            ObjectInputStream(FileInputStream(file)).use { stream ->
                stream.readObject() as? T
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}