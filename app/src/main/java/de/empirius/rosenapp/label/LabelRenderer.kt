package de.empirius.rosenapp.label

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import android.text.TextUtils
import androidx.exifinterface.media.ExifInterface
import de.empirius.rosenapp.data.Plant
import java.text.DateFormat
import java.util.Date
import kotlin.math.min

private fun mmToPx(mm: Float, dpi: Int): Int = Math.round(mm / 25.4f * dpi)
private fun mmToPt(mm: Float): Float = mm / 25.4f * 72f

/**
 * Draws a printable garden label for a [Plant] onto a [Bitmap].
 *
 * The same bitmap feeds both the on-screen preview and the PDF/PNG exporters,
 * so what the user sees is exactly what prints. Layout is a simple two-column
 * design: photo on the left, text block on the right, with a QR code tucked
 * into the bottom-right corner.
 */
object LabelRenderer {

    /** Physical label dimensions. Default is a 90 x 60 mm garden label at print quality. */
    data class Spec(
        val widthMm: Float = 90f,
        val heightMm: Float = 60f,
        val dpi: Int = 300,
    ) {
        val widthPx: Int get() = mmToPx(widthMm, dpi)
        val heightPx: Int get() = mmToPx(heightMm, dpi)
        /** Page size in PostScript points (1/72 inch) for PDF export. */
        val widthPt: Float get() = mmToPt(widthMm)
        val heightPt: Float get() = mmToPt(heightMm)
    }

    private const val INK = 0xFF202020.toInt()

    fun render(plant: Plant, spec: Spec = Spec()): Bitmap {
        val w = spec.widthPx
        val h = spec.heightPx
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val scale = spec.dpi / 300f // all literals below are tuned for 300 dpi
        val pad = 24f * scale

        // The user-chosen accent drives the border, title, divider, and section labels.
        val accent = plant.accentColor

        // Outer rounded border in the accent color.
        val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = accent
            strokeWidth = 6f * scale
        }
        val borderInset = border.strokeWidth / 2f + 4f * scale
        val radius = 28f * scale
        canvas.drawRoundRect(
            RectF(borderInset, borderInset, w - borderInset, h - borderInset),
            radius, radius, border,
        )

        // Photo column on the left (square-ish), if we have a photo.
        val photoSize = h - pad * 2
        val textLeft: Float
        if (!plant.photoPath.isNullOrBlank()) {
            val photo = loadCropped(plant.photoPath, photoSize.toInt(), photoSize.toInt())
            if (photo != null) {
                val dst = RectF(pad, pad, pad + photoSize, pad + photoSize)
                val clipPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val rounded = roundedCrop(photo, 18f * scale)
                canvas.drawBitmap(rounded, null, dst, clipPaint)
                textLeft = pad + photoSize + pad
            } else {
                textLeft = pad
            }
        } else {
            textLeft = pad
        }
        val textRight = w - pad

        // QR code in the bottom-right corner.
        val qrSize = (h * 0.30f)
        val qr = QrCodeGenerator.encode(qrPayload(plant), qrSize.toInt())
        val qrLeft = w - pad - qrSize
        val qrTop = h - pad - qrSize
        canvas.drawBitmap(qr, qrLeft, qrTop, null)

        // Title (name / variety).
        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textSize = 44f * scale
        }
        var y = pad + titlePaint.textSize
        drawEllipsized(canvas, plant.name, titlePaint, textLeft, y, textRight - textLeft)

        // Divider under the title.
        y += 14f * scale
        val divider = Paint().apply { color = accent; strokeWidth = 2f * scale }
        canvas.drawLine(textLeft, y, textRight, y, divider)

        // Detail lines.
        val labelPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 20f * scale
        }
        val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = INK
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 24f * scale
        }
        val lineGap = 12f * scale
        y += 22f * scale + valuePaint.textSize

        plant.location?.takeIf { it.isNotBlank() }?.let {
            y = drawDetail(canvas, "LOCATION", it, labelPaint, valuePaint, textLeft, y, textRight - textLeft, lineGap)
        }
        plant.plantingDateMillis?.let {
            val date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(it))
            y = drawDetail(canvas, "PLANTED", date, labelPaint, valuePaint, textLeft, y, textRight - textLeft, lineGap)
        }
        plant.careNotes?.takeIf { it.isNotBlank() }?.let {
            // Notes wrap to two lines, kept clear of the QR code.
            val notesRight = qrLeft - pad
            y = drawDetail(canvas, "NOTES", it, labelPaint, valuePaint, textLeft, y, notesRight - textLeft, lineGap, maxLines = 2)
        }

        return bitmap
    }

    private fun drawDetail(
        canvas: Canvas,
        label: String,
        value: String,
        labelPaint: TextPaint,
        valuePaint: TextPaint,
        x: Float,
        topBaseline: Float,
        maxWidth: Float,
        lineGap: Float,
        maxLines: Int = 1,
    ): Float {
        var y = topBaseline
        canvas.drawText(label, x, y, labelPaint)
        y += valuePaint.textSize + 2f
        if (maxLines <= 1) {
            drawEllipsized(canvas, value, valuePaint, x, y, maxWidth)
            y += lineGap + valuePaint.textSize
        } else {
            y = drawWrapped(canvas, value, valuePaint, x, y, maxWidth, maxLines)
            y += lineGap
        }
        return y + lineGap
    }

    private fun drawEllipsized(canvas: Canvas, text: String, paint: TextPaint, x: Float, y: Float, maxWidth: Float) {
        val out = TextUtils.ellipsize(text, paint, maxWidth, TextUtils.TruncateAt.END)
        canvas.drawText(out, 0, out.length, x, y, paint)
    }

    private fun drawWrapped(
        canvas: Canvas,
        text: String,
        paint: TextPaint,
        x: Float,
        topBaseline: Float,
        maxWidth: Float,
        maxLines: Int,
    ): Float {
        val words = text.split(" ")
        var line = StringBuilder()
        var y = topBaseline
        var lines = 0
        for (word in words) {
            val candidate = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(candidate) <= maxWidth) {
                line = StringBuilder(candidate)
            } else {
                if (lines == maxLines - 1) {
                    drawEllipsized(canvas, "$line $word", paint, x, y, maxWidth)
                    return y
                }
                canvas.drawText(line.toString(), x, y, paint)
                y += paint.textSize + 4f
                lines++
                line = StringBuilder(word)
            }
        }
        if (line.isNotEmpty()) canvas.drawText(line.toString(), x, y, paint)
        return y
    }

    /** Loads [path], applies EXIF orientation, then center-crops to [targetW] x [targetH] filling the box. */
    private fun loadCropped(path: String, targetW: Int, targetH: Int): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        if (bounds.outWidth <= 0) return null
        val sample = maxOf(1, min(bounds.outWidth / targetW, bounds.outHeight / targetH))
        val decoded = BitmapFactory.decodeFile(path, BitmapFactory.Options().apply { inSampleSize = sample })
            ?: return null
        // Cameras often store the image in sensor orientation and record the
        // intended rotation in EXIF; apply it so the photo isn't sideways.
        val full = applyExifOrientation(decoded, path)

        val scale = maxOf(targetW.toFloat() / full.width, targetH.toFloat() / full.height)
        val scaledW = (full.width * scale).toInt()
        val scaledH = (full.height * scale).toInt()
        val scaled = Bitmap.createScaledBitmap(full, scaledW, scaledH, true)
        val xOff = (scaledW - targetW) / 2
        val yOff = (scaledH - targetH) / 2
        return Bitmap.createBitmap(scaled, xOff.coerceAtLeast(0), yOff.coerceAtLeast(0), targetW, targetH)
    }

    /** Rotates/flips [bitmap] according to the JPEG EXIF orientation tag at [path]. */
    private fun applyExifOrientation(bitmap: Bitmap, path: String): Bitmap {
        val orientation = runCatching {
            ExifInterface(path).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL,
            )
        }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> { matrix.postRotate(90f); matrix.postScale(-1f, 1f) }
            ExifInterface.ORIENTATION_TRANSVERSE -> { matrix.postRotate(270f); matrix.postScale(-1f, 1f) }
            else -> return bitmap
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    private fun roundedCrop(src: Bitmap, radius: Float): Bitmap {
        val out = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(0f, 0f, src.width.toFloat(), src.height.toFloat())
        canvas.drawRoundRect(rect, radius, radius, paint)
        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, 0f, 0f, paint)
        return out
    }

    private fun qrPayload(plant: Plant): String = buildString {
        append("RosenApp plant\n")
        append("Name: ").append(plant.name).append('\n')
        plant.location?.takeIf { it.isNotBlank() }?.let { append("Location: ").append(it).append('\n') }
        plant.plantingDateMillis?.let {
            append("Planted: ").append(DateFormat.getDateInstance().format(Date(it))).append('\n')
        }
        append("id:").append(plant.id)
    }
}
