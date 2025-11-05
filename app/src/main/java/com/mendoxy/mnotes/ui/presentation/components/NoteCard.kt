package com.mendoxy.mnotes.ui.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import com.mendoxy.mnotes.ui.theme.dimenLarge
import com.mendoxy.mnotes.ui.theme.dimenMiddle

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    title: String = "Nota",
    date: String = "Hoy",
    content: (@Composable () -> Unit) = {
        DefaultText(
            text = "Nota de prueba",
            color = MaterialTheme.colorScheme.secondary
        )
    }
    ) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = dimenLarge, vertical = dimenLarge)

        ) {
            Header.CardHeader(title = title)
            Spacer(Modifier.height(dimenLarge))
            content()
            Spacer(Modifier.height(dimenLarge))
            DefaultText(
                text = date,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private object Header{
    @Composable
    fun CardHeader(modifier: Modifier = Modifier, title: String) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefaultText(
                text = title,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(R.drawable.ic_editnote), contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(dimenLarge))
                Icon(painter = painterResource(R.drawable.ic_trash), contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun prevCard() {
    MNotesTheme {
        NoteCard()
    }
}