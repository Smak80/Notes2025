package ru.smak.notes2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.smak.notes2025.ui.EditNote
import ru.smak.notes2025.ui.NoteList
import ru.smak.notes2025.ui.theme.Notes2025Theme
import kotlin.system.exitProcess
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.smak.notes2025.database.Note

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadNotes(applicationContext)

        enableEdgeToEdge()
        setContent {
            FullContent(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun FullContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
){
    Notes2025Theme {
        BackHandler {
            if (viewModel.currentView == ViewType.EDIT_MODE){
                viewModel.toListMode()
            } else {
                exitProcess(0)
            }
        }
        Scaffold(
            modifier = modifier,
            topBar = { Header() },
            floatingActionButton = { if (viewModel.currentView == ViewType.LIST_MODE) AddNoteButton() },
        ) { innerPadding ->
            MainContent(modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
){
    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.main_title)) },
        navigationIcon = {
            if (viewModel.currentView == ViewType.EDIT_MODE) {
                IconButton(onClick = {
                    viewModel.toListMode()
                }) {
                    Icon(
                        painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
){
    val notes: List<Note> = viewModel.notes?.collectAsState(listOf())?.value ?: listOf()
    Column(modifier = modifier) {
        when (viewModel.currentView) {
            ViewType.LIST_MODE -> NoteList(
                notes,
                modifier = Modifier.fillMaxSize(),
                onEditNote = {viewModel.editNote(it)},
                onDeleteNote = { viewModel.deleteNote(it)}
            )
            ViewType.EDIT_MODE -> {
                viewModel.currentNote?.let {
                    EditNote(
                        it,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: run { viewModel.currentView = ViewType.LIST_MODE }
            }
        }
    }
}

@Composable
fun AddNoteButton(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
){
    FloatingActionButton(
        onClick = {
            viewModel.addNote()
        },
        modifier = modifier,
        shape = RoundedCornerShape(100),
    ) {
        Icon(
            painterResource(R.drawable.twotone_add_circle_48),
            contentDescription = stringResource(R.string.add_note),
        )
    }
}