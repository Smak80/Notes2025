package ru.smak.notes2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.smak.notes2025.models.Note
import ru.smak.notes2025.ui.EditNote
import ru.smak.notes2025.ui.NoteList
import ru.smak.notes2025.ui.theme.Notes2025Theme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Notes2025Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { Header() },
                    floatingActionButton = { AddNoteButton() },
                ) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Header(modifier: Modifier = Modifier){
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
    private fun MainContent(modifier: Modifier = Modifier){
        Column(modifier = modifier) {
            when (viewModel.currentView) {
                ViewType.LIST_MODE -> NoteList(
                    viewModel.notes,
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
    private fun AddNoteButton(modifier: Modifier = Modifier){
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
}