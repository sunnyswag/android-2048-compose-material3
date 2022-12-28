package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexjlockwood.twentyfortyeight.R
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel()
) {
    AppTheme {
        Surface {
            var shouldShowNewGameDialog by remember { mutableStateOf(false) }
            Scaffold(
                topBar = {
                    GameTopAppBar(
                        title = stringResource(R.string.app_name),
                        onAddButtonClick = { shouldShowNewGameDialog = true }
                    )
                }
            ) { innerPadding ->
                GameUi(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    gridTileMovements = gameViewModel.gridTileMovements,
                    currentScore = gameViewModel.currentScore,
                    bestScore = gameViewModel.bestScore,
                    moveCount = gameViewModel.moveCount,
                    onSwipeListener = { direction -> gameViewModel.move(direction) },
                )
            }

            if (gameViewModel.isGameOver) {
                GameDialog(
                    title = stringResource(R.string.msg_game_over),
                    message = stringResource(R.string.msg_game_over_body),
                    onConfirmListener = { gameViewModel.startNewGame() },
                    onDismissListener = {
                        // TODO: allow user to dismiss the dialog so they can take a screenshot
                    },
                )
            }
            else if (shouldShowNewGameDialog) {
                GameDialog(
                    title = stringResource(R.string.msg_start_new_game),
                    message = stringResource(R.string.msg_start_new_game_body),
                    onConfirmListener = {
                        gameViewModel.startNewGame()
                        shouldShowNewGameDialog = false
                    },
                    onDismissListener = {
                        shouldShowNewGameDialog = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onAddButtonClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        actions = {
            IconButton(onClick = { onAddButtonClick() }) {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        }
    )
}
