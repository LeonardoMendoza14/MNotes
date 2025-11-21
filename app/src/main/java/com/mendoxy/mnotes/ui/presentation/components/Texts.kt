package com.mendoxy.mnotes.ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mendoxy.mnotes.ui.theme.dimenBig

@Composable
fun DefaultText(
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingSpacing: Dp = dimenBig,
    trailingSpacing: Dp = dimenBig,
    text: String,
    isTitle: Boolean = false,
    color: Color,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
){
    Row(
        modifier = containerModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        leadingIcon?.let { leadingIcon() }

        Spacer(Modifier.width(leadingSpacing))

        Text(
            text = text,
            color = color,
            style = if (!isTitle) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleLarge,
            modifier = modifier
        )

        Spacer(Modifier.width(trailingSpacing))

        trailingIcon?.let{ trailingIcon() }

    }
}