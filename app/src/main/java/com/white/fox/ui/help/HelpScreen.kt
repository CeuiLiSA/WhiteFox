package com.white.fox.ui.help

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.white.fox.R
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.setting.localizedString

@Composable
fun HelpScreen() = PageScreen(localizedString(R.string.help)) {
    val color = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {


        HelpLinkItem(
            title = "Pixiv Help Center",
            url = "https://www.pixiv.help/"
        )


        HelpLinkItem(
            title = "Terms of Use",
            url = "https://policies.pixiv.net/en.html#terms"
        )


        HelpLinkItem(
            title = "Privacy Policy",
            url = "https://policies.pixiv.net/en/privacy_policy.html"
        )


        HelpLinkItem(
            title = "Legal Notation",
            url = "https://policies.pixiv.net/en.html#notation"
        )


        HelpLinkItem(
            title = "Announcement",
            url = "https://www.pixiv.net/info.php"
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "所有链接均指向 Pixiv 官方站点。",
            style = MaterialTheme.typography.bodySmall,
            color = color.outline,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun HelpLinkItem(title: String, url: String) {
    val color = MaterialTheme.colorScheme
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(url) }
            .padding(vertical = 10.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = color.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = url,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
            color = color.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
