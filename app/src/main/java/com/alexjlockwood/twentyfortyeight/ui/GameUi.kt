package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    onAddButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    var swipeAngle by remember { mutableStateOf(0f) }
    BoxWithConstraints(
        modifier = modifier.pointerInput(Unit) {
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
            val (gameGridRef, currentScoreRef, bestScoreRef) = createRefs()
            val (titleRef, actionAddRef, actionBackRef) = createRefs()
            TitleBox(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(titleRef) {
                        if (isPortrait) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        } else {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                    }
            )
            ActionBox(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(actionAddRef) {
                        if (isPortrait) {
                            end.linkTo(parent.end)
                            bottom.linkTo(titleRef.bottom)
                        } else {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(titleRef.end)
                        }
                    },
                imageVector = Icons.Filled.Add,
            ) { onAddButtonClick() }
            ActionBox(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(actionBackRef) {
                        if (isPortrait) {
                            end.linkTo(actionAddRef.start)
                            bottom.linkTo(titleRef.bottom)
                        } else {
                            bottom.linkTo(actionAddRef.top)
                            end.linkTo(titleRef.end)
                        }
                    },
                imageVector = Icons.Filled.ArrowBack,
            ) { onBackButtonClick() }
            GameGrid(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .constrainAs(gameGridRef) {
                        if (isPortrait) {
                            start.linkTo(parent.start)
                            top.linkTo(titleRef.bottom)
                            bottom.linkTo(bestScoreRef.top)
                            end.linkTo(parent.end)
                        } else {
                            start.linkTo(titleRef.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(bestScoreRef.start)
                        }
                    },
                gridTileMovements = gridTileMovements,
                moveCount = moveCount,
            )
            ScoreBox(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(currentScoreRef) {
                        if (isPortrait) {
                            end.linkTo(bestScoreRef.start)
                            top.linkTo(bestScoreRef.top)
                        }
                        else {
                            start.linkTo(bestScoreRef.start)
                            bottom.linkTo(bestScoreRef.top)
                        }
                    },
                text = "$currentScore",
                label = stringResource(R.string.msg_score)
            )
            ScoreBox(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(bestScoreRef) {
                        if (isPortrait) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.bottom)
                        } else {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                    },
                text = "$bestScore",
                label = stringResource(R.string.msg_best_score)
            )
        }
    }
}

@Composable
private fun TitleBox(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.app_name),
    FontSize: TextUnit = 32.sp,
) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = text,
            fontSize = FontSize,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun ActionBox(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    Card(modifier.clickable { onClick() }) {
        Icon(
            modifier = Modifier.padding(16.dp),
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = ""
        )
    }
}

@Composable
private fun ScoreBox(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    minFontSize: TextUnit = 16.sp
) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = text,
            fontSize = minFontSize * 2,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = label,
            fontSize = minFontSize,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

private fun uatan2(x: Float, y: Float): Float =
    toDegrees(atan2(y, x).toDouble()).toFloat().let { deg ->
        if (deg < 0) { deg + 360 }
        else { deg }
    }
