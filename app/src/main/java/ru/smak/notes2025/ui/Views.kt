package ru.smak.notes2025.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    editAction: (Note)->Unit = {},
    deleteAction: (Note)->Unit = {},
){
    val date = note.creation.format(
        DateTimeFormatter.ofLocalizedDateTime(
            FormatStyle.SHORT
        )
    )

    ElevatedCard(
        modifier = modifier,
        onClick = {
            editAction(note)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            if (note.title.isNotBlank())
                Text(text = note.title,
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                )
            if (note.text.isNotBlank() && note.title.isNotBlank())
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            if (note.text.isNotBlank())
                Text(text = note.text,
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Normal,
                )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(date)
                IconButton(
                    onClick = {
                        deleteAction(note)
                    },
                )
                {
                    Icon(
                        painterResource(R.drawable.twotone_delete_24),
                        contentDescription = stringResource(R.string.add_note),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
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
fun NoteList(
    cards: List<Note>,
    modifier: Modifier = Modifier,
    onEditNote: (Note)->Unit = {},
    onDeleteNote: (Note)->Unit = {},
){
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(180.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalItemSpacing = 8.dp,
        modifier = modifier,
    ){
        items(cards) {
            NoteCard(it,
                deleteAction = { onDeleteNote(it) },
                editAction = {onEditNote(it)})
        }
    }

}

@Composable
@Preview
fun NoteListPreview(){
    Notes2025Theme {
        val cardList = listOf(
            Note("Заметка 1", "Это текст заметки №1, который будет достаточно длинным."),
            Note(text = "Это текст заметки №2, который будет достаточно длинным."),
            Note("Заметка 3"),
            Note(),
            Note("Заметка 5", "Это текст заметки №5, который будет достаточно длинным.")
        )
        NoteList(cardList)
    }
}

@Composable
fun EditNote(
    note: Note,
    modifier: Modifier = Modifier,
){
    var title by remember{ mutableStateOf(note.title) }
    var text by remember { mutableStateOf(note.text) }
    Column (
        modifier = modifier,
    ){
        OutlinedTextField(
            value = title,
            onValueChange = { title = it; note.title = title },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text(stringResource(R.string.lblTitle))
            }
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; note.text = text },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            singleLine = false,
            maxLines = 10,
            label = {
                Text(stringResource(R.string.lblText))
            }
        )
        val date = note.creation.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        OutlinedTextField(
            value = date,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            readOnly = true,
            label = {
                Text(stringResource(R.string.lblCreationTime))
            }
        )

    }
}

@Composable
@Preview
fun EditNotePreview(){
    Notes2025Theme {
        EditNote(Note())
    }
}
