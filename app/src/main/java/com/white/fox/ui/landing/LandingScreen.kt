package com.white.fox.ui.landing

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ceui.lisa.hermes.common.openCustomTab
import ceui.lisa.hermes.loadstate.LoadState
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ImageRequest
import com.white.fox.R
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.ProgressTextButton
import com.white.fox.ui.common.Route
import com.white.fox.ui.illust.withHeader
import com.white.fox.ui.setting.localizedString

@Preview
@Composable
fun LandingScreen() {
    val navViewModel = LocalNavViewModel.current
    val context = LocalContext.current
    val pkceItem = LocalDependency.current.client.pkceItem
    val sessionManager = LocalDependency.current.sessionManager
    val loginState by LocalDependency.current.client.appLoginFlow.collectAsState()
    val viewModel = viewModel<LandingViewModel>()
    val found = viewModel.foundFromPixivSessionDir.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    openCustomTab(
                        context, LOGIN_HEAD + pkceItem.challenge + LOGIN_END
                    )
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    openCustomTab(
                        context, SIGN_HEAD + pkceItem.challenge + SIGN_END
                    )
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Register")
            }

            Button(
                onClick = {
                    navViewModel.navigate(Route.LoginWithToken)
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Login via Token")
            }

            if (found != null) {
                Spacer(modifier = Modifier.height(20.dp))
                AsyncImage(
                    request = ImageRequest.Builder(
                        LocalContext.current,
                        found.user?.profile_image_urls?.findMaxSizeUrl()
                    ).withHeader().build(),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .clickable {

                        },
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = found.user?.name ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(20.dp))

                ProgressTextButton(localizedString(R.string.log_in_this_account)) {
                    sessionManager.logIn(viewModel.rawJson)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (loginState is LoadState.Loading || loginState is LoadState.Loaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingBlock()
            }
        }
    }

}

private const val LOGIN_HEAD = "https://app-api.pixiv.net/web/v1/login?code_challenge="
private const val LOGIN_END = "&code_challenge_method=S256&client=pixiv-android"


private const val SIGN_HEAD =
    "https://app-api.pixiv.net/web/v1/provisional-accounts/create?code_challenge="
private const val SIGN_END = "&code_challenge_method=S256&client=pixiv-android"