package com.mendoxy.mnotes.ui.presentation.mainScreen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.ui.presentation.components.DefaultText
import com.mendoxy.mnotes.ui.presentation.components.NoteCard
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import com.mendoxy.mnotes.ui.theme.dimenBig
import com.mendoxy.mnotes.ui.theme.dimenExtraLarge
import com.mendoxy.mnotes.ui.theme.dimenHuge
import com.mendoxy.mnotes.ui.theme.dimenLarge
import com.mendoxy.mnotes.ui.theme.dimenMiddle
import com.mendoxy.mnotes.ui.theme.dimenSmall

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    var listNotes = listOf(
        NoteCard(),
        NoteCard(),
        NoteCard(),
        NoteCard(),
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HomeTopBar.HomeTopBar()
        },
        content = {paddingValues ->
            val padding = paddingValues

        }
    )
}

private object HomeTopBar{
    @Composable
    fun HomeTopBar(){

        Column(
            modifier = Modifier
                .padding(vertical = dimenExtraLarge, horizontal = dimenLarge)
                .systemBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = dimenMiddle)
                    .fillMaxWidth()
            ) {
                DefaultText(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.primary,
                    isTitle = true
                )

                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    modifier = Modifier
                        .size(40.dp)
                        .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
                        .clip(CircleShape)
                    ,
                    contentDescription = ""
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(dimenSmall),
                modifier = Modifier
                    .padding(horizontal = dimenMiddle, vertical = dimenBig)
            ){
                DefaultText(
                    text = "0",
                    color = MaterialTheme.colorScheme.secondary
                )
                DefaultText(
                    text = "Notas",
                    color = MaterialTheme.colorScheme.secondary
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewHomeScreen(){
    MNotesTheme {
        HomeScreen(rememberNavController())
    }
}
