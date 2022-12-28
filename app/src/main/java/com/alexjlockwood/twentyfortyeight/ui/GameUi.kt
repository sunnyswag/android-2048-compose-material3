package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.alexjlockwood.twentyfortyeight.R
import com.alexjlockwood.twentyfortyeight.domain.Direction
import com.alexjlockwood.twentyfortyeight.domain.GridTileMovement
import java.lang.Math.toDegrees
import kotlin.math.atan2

/**
 * Renders the 2048 game's home screen UI.
 */
@Composable
fun GameUi(
    modifier: Modifier = Modifier,
    gridTileMovements: List<GridTileMovement>,
    currentScore: Int,
    bestScore: Int,
    moveCount: Int,
    onSwipeListener: (direction: Direction) -> Unit,
) {
    var swipeAngle by remember { mutableStateOf(0f) }
    BoxWithConstraints(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        swipeAngle = uatan2(dragAmount.x, -(dragAmount.y))
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
        ConstraintLayout {
            val (gameGrid, currentScoreText, currentScoreLabel,
                bestScoreText, bestScoreLabel) = createRefs()
            GameGrid(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .constrainAs(gameGrid) {
                        if (isPortrait) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        else {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    },
                gridTileMovements = gridTileMovements,
                moveCount = moveCount,
            )
            TextLabel(
                modifier = Modifier
                    .constrainAs(currentScoreText) {
                        if (isPortrait) {
                            start.linkTo(gameGrid.start, 16.dp)
                            top.linkTo(gameGrid.bottom)
                        }
                        else {
                            start.linkTo(currentScoreLabel.start)
                            bottom.linkTo(currentScoreLabel.top)
                        }
                    },
                text = "$currentScore",
                fontSize = 36.sp
            )
            TextLabel(
                modifier = Modifier
                    .constrainAs(currentScoreLabel) {
                        if (isPortrait) {
                            start.linkTo(currentScoreText.start)
                            top.linkTo(currentScoreText.bottom)
                        }
                        else {
                            start.linkTo(bestScoreText.start)
                            bottom.linkTo(bestScoreText.top)
                        }
                    },
                text = stringResource(R.string.msg_score),
                fontSize = 18.sp
            )
            TextLabel(
                modifier = Modifier
                    .constrainAs(bestScoreText) {
                        if (isPortrait) {
                            end.linkTo(gameGrid.end, 16.dp)
                            top.linkTo(gameGrid.bottom)
                        }
                        else {
                            start.linkTo(bestScoreLabel.start)
                            bottom.linkTo(bestScoreLabel.top)
                        }
                    },
                text = "$bestScore",
                fontSize = 36.sp
            )
            TextLabel(
                modifier = Modifier
                    .constrainAs(bestScoreLabel) {
                        if (isPortrait) {
                            end.linkTo(bestScoreText.end)
                            top.linkTo(bestScoreText.bottom)
                        }
                        else {
                            start.linkTo(gameGrid.end)
                            bottom.linkTo(gameGrid.bottom, 16.dp)
                        }
                    },
                text = stringResource(R.string.msg_best_score),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun TextLabel(modifier: Modifier, text: String, fontSize: TextUnit) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = FontWeight.Light,
    )
}

private fun uatan2(x: Float, y: Float): Float =
    toDegrees(atan2(y, x).toDouble()).toFloat().let { deg ->
        if (deg < 0) { deg + 360 }
        else deg
    }
