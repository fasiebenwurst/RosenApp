package de.empirius.rosenapp.label

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.min

/**
 * Prints a pre-rendered label [Bitmap] by drawing it onto the print page
 * ourselves.
 *
 * We deliberately avoid `PrintHelper`, which auto-rotates a landscape image to
 * fill the printer's default (often portrait) page — that's what made the label
 * appear sideways in the print dialog. Here we draw the label centered and
 * scaled-to-fit in its natural orientation, so it always prints upright
 * regardless of the selected paper size/orientation.
 */
class LabelPrintAdapter(
    private val context: Context,
    private val jobName: String,
    private val bitmap: Bitmap,
) : PrintDocumentAdapter() {

    private var attributes: PrintAttributes? = null

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback,
        extras: Bundle?,
    ) {
        attributes = newAttributes
        if (cancellationSignal?.isCanceled == true) {
            callback.onLayoutCancelled()
            return
        }
        val info = PrintDocumentInfo.Builder("$jobName.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_PHOTO)
            .setPageCount(1)
            .build()
        callback.onLayoutFinished(info, newAttributes != oldAttributes)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback,
    ) {
        val pdf = PrintedPdfDocument(context, attributes ?: return callback.onWriteFailed("No attributes"))
        val page = pdf.startPage(0)
        try {
            if (cancellationSignal?.isCanceled == true) {
                callback.onWriteCancelled()
                return
            }
            val canvas = page.canvas // sized to the printable content area, in points
            val cw = canvas.width.toFloat()
            val ch = canvas.height.toFloat()
            // Fit the label inside the page, preserving aspect ratio. No rotation.
            val scale = min(cw / bitmap.width, ch / bitmap.height)
            val dw = bitmap.width * scale
            val dh = bitmap.height * scale
            val left = (cw - dw) / 2f
            val top = (ch - dh) / 2f
            canvas.drawBitmap(bitmap, null, RectF(left, top, left + dw, top + dh), Paint(Paint.FILTER_BITMAP_FLAG))
            pdf.finishPage(page)

            FileOutputStream(destination.fileDescriptor).use { pdf.writeTo(it) }
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: IOException) {
            callback.onWriteFailed(e.message)
        } finally {
            pdf.close()
        }
    }
}
