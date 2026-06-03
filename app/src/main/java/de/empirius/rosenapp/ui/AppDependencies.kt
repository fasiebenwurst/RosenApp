package de.empirius.rosenapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import de.empirius.rosenapp.RosenApplication

/** Convenience accessor for the [RosenApplication] singleton from Compose. */
@Composable
fun rememberApp(): RosenApplication {
    val context = LocalContext.current
    return remember(context) { context.applicationContext as RosenApplication }
}
