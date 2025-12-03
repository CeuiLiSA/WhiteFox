package com.white.fox.ui.help

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.white.fox.BuildConfig
import com.white.fox.R
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.setting.localizedString

@Composable
fun AboutScreen() = PageScreen(localizedString(R.string.about_app)) {
    val color = MaterialTheme.colorScheme
    val versionName = BuildConfig.VERSION_NAME

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_splash_center),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "WhiteFox",
            style = MaterialTheme.typography.headlineSmall,
            color = color.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "版本号：$versionName",
            style = MaterialTheme.typography.bodyMedium,
            color = color.outline,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(28.dp))

        SectionTitle("应用简介")

        Text(
            text = "WhiteFox 是一款由 Compose 构建的 Pixiv 第三方客户端，致力于提供流畅、现代、美观的浏览体验。项目完全开源，欢迎贡献与交流。",
            style = MaterialTheme.typography.bodyMedium,
            color = color.onSurfaceVariant,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        SectionTitle("开源地址")
        LinkItem("GitHub", "https://github.com/CeuiLiSA/WhiteFox")

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle("联系方式")
        InfoItem("Email", "fatemercis@gmail.com")
        InfoItem("Telegram", "https://t.me/joinchat/QBTiWBvo-jda7SEl4VgK-Q")
        InfoItem("QQ", "133955579")

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "© 2025 WhiteFox. MIT Licensed.",
            style = MaterialTheme.typography.bodySmall,
            color = color.outline,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun InfoItem(title: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$title：",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LinkItem(title: String, url: String) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(url) }
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "$title：",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = url,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )
    }
}
