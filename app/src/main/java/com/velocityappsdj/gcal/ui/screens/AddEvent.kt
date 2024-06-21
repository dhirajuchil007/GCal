package com.velocityappsdj.gcal.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velocityappsdj.gcal.ui.theme.GCalTheme
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddEvent(
    modifier: Modifier = Modifier,
    defaultDate: LocalDateTime? = null,
    navigateBack: () -> Unit
) {
    Scaffold(topBar = {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = navigateBack) {
                Icon(Icons.Filled.Close, "Back")
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            var eventNameState by remember {
                mutableStateOf("")
            }

            BasicTextField(
                value = eventNameState,
                onValueChange = {
                    eventNameState = it
                },
                decorationBox = {
                    Text(
                        text = "Event name",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        color = Color.Gray
                    )
                }

            )
        }
    }
}

@Preview
@Composable
private fun AddEventPreview() {
    GCalTheme {
        AddEvent(defaultDate = LocalDateTime.now()) {}
    }
}