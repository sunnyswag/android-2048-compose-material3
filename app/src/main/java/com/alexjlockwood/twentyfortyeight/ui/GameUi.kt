package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.alexjlockwood.twentyfortyeight.domain.Direction
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import com.alexjlockwood.twentyfortyeight.R
import kotlin.math.sqrt

/**
 * Renders the 2048 game's home screen UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameUi(
    modifier: Modifier = Modifier,
    gridTileMovements: List<GridTileMovement>,
    currentScore: Int,
    bestScore: Int,
    moveCount: Int,
    isGameOver: Boolean,
    onNewGameRequested: () -> Unit,
    onSwipeListener: (direction: Direction) -> Unit,
) {
    var swipeAngle by remember { mutableStateOf(0f) }
    var shouldShowNewGameDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                modifier = modifier,
                actions = {
                    IconButton(onClick = { shouldShowNewGameDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            swipeAngle = atan2(dragAmount.x, -(dragAmount.y))
                        },
                        onDragEnd = {
                            onSwipeListener(
                                when {
                                    45 <= swipeAngle && swipeAngle < 135 -> Direction.NORTH
                                    135 <= swipeAngle && swipeAngle < 225 -> Direction.WEST
                                    225 <= swipeAngle && swipeAngle < 315 -> Direction.SOUTH
                                    else -> Direction.EAST
                                }
                            )
                        }
                    )
                }
        ) {
            val isPortrait = maxWidth < maxHeight
            ConstraintLayout(
                constraintSet = buildConstraints(isPortrait)
            ) {
                GameGrid(
                    modifier = modifier
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .layoutId("gameGrid"),
                    gridTileMovements = gridTileMovements,
                    moveCount = moveCount,
                )
                TextLabel(text = "$currentScore", layoutId = "currentScoreText", fontSize = 36.sp)
                TextLabel(text = "Score", layoutId = "currentScoreLabel", fontSize = 18.sp)
                TextLabel(text = "$bestScore", layoutId = "bestScoreText", fontSize = 36.sp)
                TextLabel(text = "Best", layoutId = "bestScoreLabel", fontSize = 18.sp)
            }
        }
    }
    if (isGameOver) {
        GameDialog(
            title = "Game over",
            message = "Start a new game?",
            onConfirmListener = { onNewGameRequested.invoke() },
            onDismissListener = {
                // TODO: allow user to dismiss the dialog so they can take a screenshot
            },
        )
    } else if (shouldShowNewGameDialog) {
        GameDialog(
            title = "Start a new game?",
            message = "Starting a new game will erase your current game",
            onConfirmListener = {
                onNewGameRequested.invoke()
                shouldShowNewGameDialog = false
            },
            onDismissListener = {
                shouldShowNewGameDialog = false
            },
        )
    }
}

@Composable
private fun TextLabel(text: String, layoutId: String, fontSize: TextUnit) {
    Text(
        text = text,
        modifier = Modifier.layoutId(layoutId),
        fontSize = fontSize,
        fontWeight = FontWeight.Light,
    )
}

private fun buildConstraints(isPortrait: Boolean): ConstraintSet {
    return ConstraintSet {
        val gameGrid = createRefFor("gameGrid")
        val currentScoreText = createRefFor("currentScoreText")
        val currentScoreLabel = createRefFor("currentScoreLabel")
        val bestScoreText = createRefFor("bestScoreText")
        val bestScoreLabel = createRefFor("bestScoreLabel")

        if (isPortrait) {
            constrain(gameGrid) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
            constrain(currentScoreText) {
                start.linkTo(gameGrid.start, 16.dp)
                top.linkTo(gameGrid.bottom)
            }
            constrain(currentScoreLabel) {
                start.linkTo(currentScoreText.start)
                top.linkTo(currentScoreText.bottom)
            }
            constrain(bestScoreText) {
                end.linkTo(gameGrid.end, 16.dp)
                top.linkTo(gameGrid.bottom)
            }
            constrain(bestScoreLabel) {
                end.linkTo(bestScoreText.end)
                top.linkTo(bestScoreText.bottom)
            }
        } else {
            constrain(gameGrid) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            constrain(currentScoreText) {
                start.linkTo(currentScoreLabel.start)
                bottom.linkTo(currentScoreLabel.top)
            }
            constrain(currentScoreLabel) {
                start.linkTo(bestScoreText.start)
                bottom.linkTo(bestScoreText.top)
            }
            constrain(bestScoreText) {
                start.linkTo(bestScoreLabel.start)
                bottom.linkTo(bestScoreLabel.top)
            }
            constrain(bestScoreLabel) {
                start.linkTo(gameGrid.end)
                bottom.linkTo(gameGrid.bottom, 16.dp)
            }
            createHorizontalChain(gameGrid, bestScoreLabel, chainStyle = ChainStyle.Packed)
        }
    }
}

private fun atan2(x: Float, y: Float): Float {
    var degrees = Math.toDegrees(kotlin.math.atan2(y, x).toDouble()).toFloat()
    if (degrees < 0) {
        degrees += 360
    }
    return degrees
}
