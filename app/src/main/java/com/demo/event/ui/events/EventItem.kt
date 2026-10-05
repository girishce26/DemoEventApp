package com.demo.event.ui.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.demo.event.domain.model.Event

@Composable
fun EventItem(
    event: Event,
    distance: String?,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
            .clickable {
                onClick()
            }
    ) {

        Column {

            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = event.title,
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text = event.location,
                        style =
                            MaterialTheme.typography
                                .bodyMedium
                    )

                    Text(
                        text = event.time,
                        style =
                            MaterialTheme.typography
                                .bodySmall
                    )

                    distance?.let {

                        Text(
                            text = it,
                            style =
                                MaterialTheme.typography
                                    .bodySmall
                        )
                    }
                }

                IconButton(
                    onClick = onBookmarkClick
                ) {

                    Icon(
                        imageVector =
                            if (event.isBookmarked)
                                Icons.Default.Bookmark
                            else
                                Icons.Default.BookmarkBorder,

                        contentDescription =
                            if (event.isBookmarked)
                                "Remove bookmark"
                            else
                                "Bookmark event"
                    )
                }
            }
        }
    }
}