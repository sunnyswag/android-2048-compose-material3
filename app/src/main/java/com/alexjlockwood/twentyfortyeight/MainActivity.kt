package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.alexjlockwood.twentyfortyeight.ui.AppTheme
import com.alexjlockwood.twentyfortyeight.ui.GameUi
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameViewModel by viewModels<GameViewModel> { GameViewModelFactory(this) }

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
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
    }
}
