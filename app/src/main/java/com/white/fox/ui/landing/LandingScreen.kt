package com.white.fox.ui.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ceui.lisa.hermes.common.PKCEItem
import ceui.lisa.hermes.common.openCustomTab
import ceui.lisa.hermes.loadstate.LoadState
import com.white.fox.ui.common.LoadingBlock
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.Route

@Preview
@Composable
fun LandingScreen() {
    val navViewModel = LocalNavViewModel.current
    val context = LocalContext.current
    val pkceItem = LocalDependency.current.client.pkceItem

    val loginState by LocalDependency.current.client.appLoginFlow.collectAsState()

    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loginState is LoadState.Loading || loginState is LoadState.Loaded) {
            LoadingBlock()
        } else {
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
        }

    }
}

private const val LOGIN_HEAD = "https://app-api.pixiv.net/web/v1/login?code_challenge="
private const val LOGIN_END = "&code_challenge_method=S256&client=pixiv-android"


private const val SIGN_HEAD = "https://app-api.pixiv.net/web/v1/provisional-accounts/create?code_challenge="
private const val SIGN_END = "&code_challenge_method=S256&client=pixiv-android"