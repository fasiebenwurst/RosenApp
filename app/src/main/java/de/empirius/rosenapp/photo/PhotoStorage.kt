package de.empirius.rosenapp.photo

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

/**
 * Manages plant photos on disk. Photos live in the app's internal
 * `files/photos/` directory; we hand the camera app a [FileProvider] URI to
 * write into and persist the resulting file path on the [de.empirius.rosenapp.data.Plant].
 */
class PhotoStorage(private val context: Context) {

    private val authority: String
        get() = "${context.packageName}.fileprovider"

    private val photosDir: File
        get() = File(context.filesDir, "photos").apply { mkdirs() }

    /** Creates a fresh empty file and returns both its path and a shareable content URI. */
    fun newPhotoTarget(): PhotoTarget {
        val file = File(photosDir, "rose_${UUID.randomUUID()}.jpg")
        val uri = FileProvider.getUriForFile(context, authority, file)
        return PhotoTarget(path = file.absolutePath, uri = uri)
    }

    /** Copies an externally-picked image (e.g. from the gallery) into our managed storage. */
    fun importFrom(source: Uri): String? {
        val file = File(photosDir, "rose_${UUID.randomUUID()}.jpg")
        return runCatching {
            context.contentResolver.openInputStream(source)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            file.absolutePath
        }.getOrNull()
    }

    fun deletePhoto(path: String?) {
        if (path.isNullOrBlank()) return
        runCatching { File(path).takeIf { it.exists() }?.delete() }
    }

    data class PhotoTarget(val path: String, val uri: Uri)
}
