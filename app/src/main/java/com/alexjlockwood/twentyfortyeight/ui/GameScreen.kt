package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel()
) {
    AppTheme {
        Surface {
            GameUi(
                gridTileMovements = gameViewModel.gridTileMovements,
                currentScore = gameViewModel.currentScore,
                bestScore = gameViewModel.bestScore,
                moveCount = gameViewModel.moveCount,
                isGameOver = gameViewModel.isGameOver,
                onNewGameRequested = { gameViewModel.startNewGame() },
                onSwipeListener = { direction -> gameViewModel.move(direction) },
            )
        }
    }
}
