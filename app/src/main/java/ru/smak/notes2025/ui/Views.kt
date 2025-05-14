package ru.smak.notes2025.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.smak.notes2025.R
import ru.smak.notes2025.database.Note
import ru.smak.notes2025.ui.theme.Notes2025Theme
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    editAction: (Note)->Unit = {},
    deleteAction: (Note)->Unit = {},
){
    val context = LocalContext.current
    val currentNote by rememberUpdatedState(note)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    deleteAction(note)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    deleteAction(note)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .75f },
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            DismissBackground(dismissState)
        },
        content = {
            NoteCard(currentNote, editAction = editAction)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart,
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.error
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Card {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                Icon(
                    Icons.TwoTone.Delete,
                    contentDescription = "delete"
                )
            }
            Spacer(modifier = Modifier)
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Icon(
                    Icons.TwoTone.Delete,
                    contentDescription = "delete"
                )
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    editAction: (Note)->Unit = {},
){
    val date = note.creationTime.format(
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
                Text(
                    text = note.title,
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
                Text(
                    text = note.text,
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Normal,
                )
            Text(date)
        }
    }
}



@Composable
@Preview
fun NoteCardPreview(){
    Notes2025Theme {
        NoteCard(Note(0, "Заметка 1", "Это текст заметки №1, который будет достаточно длинным."))
    }
}

@Composable
fun NoteList(
    cards: List<Note>,
    modifier: Modifier = Modifier,
    onEditNote: (Note)->Unit = {},
    onDeleteNote: (Note)->Unit = {},
){
    Filter(
        modifier = Modifier.fillMaxWidth()
    )
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(180.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(8.dp),
        modifier = modifier,
    ){
        items(cards.reversed()) {
            NoteItem(it,
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
            Note(0, "Заметка 1", "Это текст заметки №1, который будет достаточно длинным."),
            Note(text = "Это текст заметки №2, который будет достаточно длинным."),
            Note(0, "Заметка 3"),
            Note(),
            Note(0, "Заметка 5", "Это текст заметки №5, который будет достаточно длинным.")
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
        val date = note.creationTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter(
    modifier: Modifier = Modifier,
){
    var query by remember {mutableStateOf("")}
    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onSearch = {},
                onQueryChange = {query = it},
                expanded = false,
                onExpandedChange = {},
                placeholder = {  },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        )
    ) { }
}