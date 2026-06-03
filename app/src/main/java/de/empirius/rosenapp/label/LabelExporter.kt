package de.empirius.rosenapp.label

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import de.empirius.rosenapp.data.Plant
import java.io.File
import java.io.FileOutputStream

/**
 * Turns a [Plant]'s rendered label into shareable PDF and PNG files, and wires
 * up the Android share sheet. Exports land in `cacheDir/labels/` and are handed
 * out through the app's [FileProvider].
 */
class LabelExporter(private val context: Context) {

    private val authority: String get() = "${context.packageName}.fileprovider"

    private val labelsDir: File
        get() = File(context.cacheDir, "labels").apply { mkdirs() }

    /** Renders [plant] to a PNG and returns the file. */
    fun exportPng(plant: Plant, spec: LabelRenderer.Spec = LabelRenderer.Spec()): File {
        val bitmap = LabelRenderer.render(plant, spec)
        val file = File(labelsDir, "${fileBase(plant)}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }

    /** Renders [plant] to a single-page PDF sized to the physical label and returns the file. */
    fun exportPdf(plant: Plant, spec: LabelRenderer.Spec = LabelRenderer.Spec()): File {
        val bitmap = LabelRenderer.render(plant, spec)
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(
            Math.round(spec.widthPt),
            Math.round(spec.heightPt),
            1,
        ).create()
        val page = document.startPage(pageInfo)
        val dst = RectF(0f, 0f, spec.widthPt, spec.heightPt)
        page.canvas.drawBitmap(bitmap, null, dst, Paint(Paint.FILTER_BITMAP_FLAG))
        document.finishPage(page)

        val file = File(labelsDir, "${fileBase(plant)}.pdf")
        FileOutputStream(file).use { out -> document.writeTo(out) }
        document.close()
        return file
    }

    fun uriFor(file: File): Uri = FileProvider.getUriForFile(context, authority, file)

    /** Opens the system share sheet for an already-exported label file. */
    fun share(file: File) {
        val uri = uriFor(file)
        val mime = if (file.extension == "pdf") "application/pdf" else "image/png"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, "Share label").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            },
        )
    }

    private fun fileBase(plant: Plant): String {
        val safe = plant.name.ifBlank { "rose" }
            .lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .trim('_')
            .ifBlank { "rose" }
        return "label_${safe}_${plant.id}"
    }

    /** Drop any previously exported files for a deleted plant. */
    fun cleanupFor(plant: Plant) {
        val suffixes = listOf("_${plant.id}.png", "_${plant.id}.pdf")
        labelsDir.listFiles()
            ?.filter { file -> suffixes.any { file.name.endsWith(it) } }
            ?.forEach { it.delete() }
    }
}
