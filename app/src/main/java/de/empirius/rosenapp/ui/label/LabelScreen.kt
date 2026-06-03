package de.empirius.rosenapp.ui.label

import android.content.Context
import android.graphics.Bitmap
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import de.empirius.rosenapp.R
import de.empirius.rosenapp.data.Plant
import de.empirius.rosenapp.label.LabelPrintAdapter
import de.empirius.rosenapp.label.LabelRenderer
import de.empirius.rosenapp.ui.rememberApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelScreen(
    plantId: Long,
    onBack: () -> Unit,
) {
    val app = rememberApp()
    val context = LocalContext.current
    val viewModel: LabelViewModel = viewModel(
        factory = viewModelFactory {
            initializer { LabelViewModel(plantId, app.repository, app.labelExporter) }
        },
    )
    val plant by viewModel.plant.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.label_preview_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { inner ->
        val current = plant
        if (current == null) {
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Render a screen-resolution preview off the main thread.
        val previewBitmap by produceState<Bitmap?>(initialValue = null, current) {
            value = withContext(Dispatchers.Default) {
                LabelRenderer.render(current, LabelRenderer.Spec(dpi = 150))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LabelPreview(previewBitmap)

            Text(
                text = stringResource(R.string.label_preview_title) + " · 90 × 60 mm",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = { viewModel.exportAndSharePdf() },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.export_pdf))
                }
                OutlinedButton(
                    onClick = { viewModel.exportAndSharePng() },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.export_png))
                }
            }

            OutlinedButton(
                onClick = { printLabel(context, current) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Outlined.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.print_label))
            }
        }
    }
}

@Composable
private fun LabelPreview(bitmap: Bitmap?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(90f / 60f)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            CircularProgressIndicator()
        }
    }
}

/**
 * Sends the label to Android's print framework. We render a high-resolution
 * bitmap and hand it to [LabelPrintAdapter], which draws it upright (no
 * auto-rotation). Defaulting to landscape lets the wide label print larger,
 * while the user can still pick any paper size in the dialog.
 */
private fun printLabel(context: Context, plant: Plant) {
    val bitmap = LabelRenderer.render(plant, LabelRenderer.Spec())
    val jobName = "Label · ${plant.name}"
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
    val attributes = PrintAttributes.Builder()
        .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE)
        .build()
    printManager.print(jobName, LabelPrintAdapter(context, jobName, bitmap), attributes)
}
