package com.example.composemvi.presentation.ui.common

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.composemvi_sample.R

@Composable
fun AlertDialog(title: String, message: String, onConfirmClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onConfirmClick,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onConfirmClick) {
                Text(stringResource(id = R.string.cancel))
            }
        },
    )
}

fun showCustomToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}
