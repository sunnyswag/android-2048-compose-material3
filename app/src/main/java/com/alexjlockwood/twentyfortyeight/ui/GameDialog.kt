package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun GameDialog(
    title: String,
    message: String,
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = { TextButton(onClick = { onConfirmListener.invoke() }) { Text("OK") } },
        dismissButton = { TextButton(onClick = { onDismissListener.invoke() }) { Text("Cancel") } },
        onDismissRequest = { onDismissListener.invoke() },
    )
}
