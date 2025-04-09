package ru.smak.notes2025.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.smak.notes2025.R
import ru.smak.notes2025.models.Note
import ru.smak.notes2025.ui.theme.Notes2025Theme

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    editAction: (Note)->Unit = {},
    deleteAction: (Note)->Unit = {},
){
    ElevatedCard(modifier = modifier, onClick = {
        editAction(note)
    }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = note.title,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(text = note.text,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
            )
            IconButton(
                onClick = {
                    deleteAction(note)
                },
                modifier.align(Alignment.End)
            ) {
                Icon(
                    painterResource(R.drawable.twotone_delete_24),
                    contentDescription = stringResource(R.string.add_note),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}



@Composable
@Preview
fun NoteCardPreview(){
    Notes2025Theme {
        NoteCard(Note("Заметка 1", "Это текст заметки №1, который будет достаточно длинным."))
    }
}

@Composable
fun NoteList(){
    LazyVerticalGrid() {  }
}

