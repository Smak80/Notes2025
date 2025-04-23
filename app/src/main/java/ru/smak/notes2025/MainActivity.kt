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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.smak.notes2025.ui.EditNote
import ru.smak.notes2025.ui.NoteList
import ru.smak.notes2025.ui.theme.Notes2025Theme
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Notes2025Theme {
                BackHandler {
                    if (viewModel.currentView == ViewType.EDIT_MODE){
                        viewModel.toListMode()
                    } else {
                        exitProcess(0)
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
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
                    },
                    floatingActionButton = {
                        if (viewModel.currentView == ViewType.LIST_MODE) {
                            FloatingActionButton(onClick = {
                                viewModel.addNote()
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_add_box_48),
                                    contentDescription = stringResource(R.string.add_note),
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
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
            }
        }
    }
}

