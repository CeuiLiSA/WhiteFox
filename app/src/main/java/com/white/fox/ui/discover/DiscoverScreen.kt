package com.white.fox.ui.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun DiscoverScreen() {
    Column {
        SectionBlock(DiscoverSection("今日榜单", listOf()), {}, {})
        SectionBlock(DiscoverSection("热门标签", listOf()), {}, {})
        SectionBlock(DiscoverSection("推荐用户", listOf()), {}, {})
        SectionBlock(DiscoverSection("最新作品", listOf()), {}, {})
        SectionBlock(DiscoverSection("Pixivision", listOf()), {}, {})
    }
}