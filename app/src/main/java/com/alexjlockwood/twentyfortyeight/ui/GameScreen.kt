package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexjlockwood.twentyfortyeight.R
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel()
) {
    AppTheme {
        Surface {
            var shouldShowNewGameDialog by remember { mutableStateOf(false) }
            GameUi(
                modifier = Modifier.fillMaxSize(),
                gridTileMovements = gameViewModel.gridTileMovements,
                currentScore = gameViewModel.currentScore,
                bestScore = gameViewModel.bestScore,
                moveCount = gameViewModel.moveCount,
                onSwipeListener = { direction -> gameViewModel.move(direction) },
                onAddButtonClick = { shouldShowNewGameDialog = true },
                onBackButtonClick = { gameViewModel.restore() },
            )

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
