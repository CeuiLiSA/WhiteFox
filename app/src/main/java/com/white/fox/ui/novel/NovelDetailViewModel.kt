package com.white.fox.ui.novel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.GeneralEntity
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Novel
import ceui.lisa.models.PixivHtmlObject
import ceui.lisa.models.WebNovel
import com.white.fox.ui.common.Dependency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber

class NovelDetailViewModel(private val novelId: Long, private val dep: Dependency) : ViewModel(),
    RefreshOwner<WebNovel> {

    private val _valueFlow = MutableStateFlow<WebNovel?>(null)
    private val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))

    fun insertViewHistory(novel: Novel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    dep.database.generalDao().insert(
                        GeneralEntity(
                            novelId,
                            gson.toJson(novel),
                            EntityType.NOVEL,
                            RecordType.VIEW_NOVEL_HISTORY
                        )
                    )
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }
    }

    private fun parsePixivObject(html: String): PixivHtmlObject? {
        // 使用Jsoup解析HTML字符串
        val doc: Document = Jsoup.parse(html)

        // 提取所有<script>标签的内容
        val scripts = doc.getElementsByTag("script")

        // 寻找包含 'Object.defineProperty(window, 'pixiv'' 的脚本
        for (script in scripts) {
            val scriptContent = script.html()

            // 查找包含 'Object.defineProperty(window, 'pixiv' 的部分
            if (scriptContent.contains("Object.defineProperty(window, 'pixiv'")) {
                // 提取 pixiv 对象字符串（这里假设 scriptContent 是 JSON 格式）
                val start = scriptContent.indexOf("value: {") + 7
                val end = scriptContent.indexOf("});", start)
                val regex = ",(?=\\s*[}\\]])".toRegex()
                val pixivJson = scriptContent.substring(start, end).trim().replace(regex, "")
                // 使用 Gson 将字符串解析为 Kotlin 对象
                return gson.fromJson(pixivJson, PixivHtmlObject::class.java)
            }
        }

        return null
    }

    override fun refresh(reason: LoadReason) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadStateFlow.value = LoadState.Loading(reason)
                val html = dep.client.appApi.getNovelText(novelId).string()
                val wNovel = parsePixivObject(html)?.novel
                _valueFlow.value = wNovel
                _loadStateFlow.value = LoadState.Loaded(wNovel != null)
            } catch (ex: Exception) {
                Timber.e(ex)
                _loadStateFlow.value = LoadState.Error(ex)
            }

        }
    }

    override val loadState: StateFlow<LoadState> = _loadStateFlow
    override val valueFlow: StateFlow<WebNovel?> = _valueFlow


    init {
        val novel = ObjectPool.get<Novel>(novelId).value
        if (novel != null) {
            insertViewHistory(novel)
        }
        refresh(LoadReason.InitialLoad)
    }
}