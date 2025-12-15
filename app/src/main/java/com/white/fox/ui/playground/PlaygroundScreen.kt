package com.white.fox.ui.playground

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceui.lisa.hermes.common.localizedString
import com.white.fox.R
import com.white.fox.ui.common.LocalSharedTransitionScope
import com.white.fox.ui.common.PageScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlaygroundScreen() = PageScreen(localizedString(R.string.app_playground)) {

    var showDetail by remember { mutableStateOf(false) }
    AnimatedContent(targetState = showDetail, label = "demo") { target ->
        if (!target) {
            ListContent(
                onClick = { showDetail = true },
                animatedVisibilityScope = this
            )
        } else {
            DetailContent(
                onBack = { showDetail = false },
                animatedVisibilityScope = this
            )
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ListContent(
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            with(sharedTransitionScope) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_user_gift),
                    contentDescription = "Sample",
                    modifier = Modifier
                        .clickable { onClick() }
                        .sharedElement(
                            rememberSharedContentState(key = "heroImage"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 文本 shared element ---
            with(sharedTransitionScope) {
                Text(
                    text = "Tap image to enlarge",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "heroText"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailContent(
    onBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            with(sharedTransitionScope) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_user_gift),
                    contentDescription = "Sample Large",
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "heroImage"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .size(300.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            with(sharedTransitionScope) {
                Text(
                    text = "Tap here to go back",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedElement(
                            rememberSharedContentState(key = "heroText"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .clickable { onBack() }
                        .padding(16.dp),
                )
            }
        }
    }
}
