package com.example.composemvi.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun BookImage(painter: Painter, contentDescription: String?, modifier: Modifier) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
