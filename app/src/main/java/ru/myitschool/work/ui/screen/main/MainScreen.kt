package ru.myitschool.work.ui.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.myitschool.work.core.TestIds

@Composable
fun MainScreen(
    state: MainState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onBookClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (state) {
            is MainState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is MainState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag(TestIds.Main.ERROR)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRefresh,
                        modifier = Modifier.testTag(TestIds.Main.REFRESH_BUTTON)
                    ) {
                        Text("Обновить")
                    }
                }
            }

            is MainState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = state.photoUrl,
                            contentDescription = "Аватар",
                            modifier = Modifier
                                .size(56.dp)
                                .testTag(TestIds.Main.PROFILE_IMAGE)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = state.name,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.testTag(TestIds.Main.PROFILE_NAME)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .testTag(TestIds.Main.REFRESH_BUTTON),
                            onClick = onRefresh
                        ) {
                            Text("Обновить")
                        }
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .testTag(TestIds.Main.LOGOUT_BUTTON),
                            onClick = onLogout
                        ) {
                            Text("Выйти")
                        }
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .testTag(TestIds.Main.ADD_BUTTON),
                            onClick = onBookClick
                        ) {
                            Text("Забронировать")
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.bookings) { index, item ->
                            BookingRow(item = item, index = index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AsyncImage(model: String, contentDescription: String, modifier: Modifier) {
}

@Composable
private fun BookingRow(item: BookingItem, index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestIds.Main.getIdItemByPosition(index))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.date,
                modifier = Modifier.testTag(TestIds.Main.ITEM_DATE)
            )
            Text(
                text = item.place,
                modifier = Modifier.testTag(TestIds.Main.ITEM_PLACE)
            )
        }
    }
}