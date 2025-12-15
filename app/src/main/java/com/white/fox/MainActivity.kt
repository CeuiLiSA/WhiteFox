package com.white.fox

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import ceui.lisa.hermes.common.LocalAppLocaleContext
import ceui.lisa.hermes.common.saveJsonToDownloads
import ceui.lisa.hermes.db.EntityType
import ceui.lisa.hermes.db.RecordType
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.viewmodel.constructVM
import com.white.fox.ui.common.Dependency
import com.white.fox.ui.common.LinkHandler
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.NavHost
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.theme.WhiteFoxTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val navViewModel by constructVM({ requireSessionManager() }) { sessionManager ->
        NavViewModel(sessionManager)
    }
    private val linkHandler by lazy {
        LinkHandler(navViewModel, (application as ServiceProvider).client::loginWithUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val serviceProvider = (application as? ServiceProvider) ?: throw ServiceProviderException()
        val dependency = Dependency(
            database = serviceProvider.database,
            client = serviceProvider.client,
            sessionManager = serviceProvider.sessionManager,
            settingsManager = serviceProvider.settingsManager,
            prefStore = serviceProvider.prefStore,
            sketch = serviceProvider.sketch,
            applicationSession = serviceProvider.applicationSession,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            lifecycleScope.launch {
                serviceProvider.sessionManager.stateFlow.collect { session ->
                    if (serviceProvider.sessionManager.loggedInUid() > 0L) {
                        try {
                            val json = gson.toJson(session)
                            saveJsonToDownloads(this@MainActivity, jsonContent = json)
                        } catch (ex: Exception) {
                            Timber.e(ex)
                        }
                    }
                }
            }
        }

        setContent {

            val settingsState = dependency.settingsManager.stateFlow.collectAsState()
            val currentLocale = settingsState.value?.language?.locale ?: Locale.getDefault()
            val localizedConfigurationContext = remember(currentLocale) {
                val config = Configuration(resources.configuration)
                config.setLocale(currentLocale)
                createConfigurationContext(config)
            }

            WhiteFoxTheme {
                CompositionLocalProvider(
                    LocalDependency provides dependency,
                    LocalNavViewModel provides navViewModel,
                    LocalAppLocaleContext provides localizedConfigurationContext,
                ) {
                    NavHost()
                }
            }
        }

        handleIntent(intent)


        MainScope().launch(Dispatchers.IO) {
            val list = dependency.database.generalDao()
                .getAllByEntityType(EntityType.APP_SESSION)

            val groups = list
                .groupBy { it.id }
                .mapValues { (_, records) ->
                    records.sortedBy { it.updatedTime }
                }

            Timber.d("[APP_SESSION] totalRecords=${list.size}, sessions=${groups.size}")

            groups.forEach { (id, records) ->
                val firstStart = records.firstOrNull {
                    it.recordType == RecordType.START_APP_SESSION
                }

                val lastStop = records.lastOrNull {
                    it.recordType == RecordType.STOP_APP_SESSION
                }

                val durationMs = when {
                    firstStart == null -> {
                        Timber.w("   [APP_SESSION][id=$id] no START record")
                        null
                    }

                    lastStop != null -> {
                        lastStop.updatedTime - firstStart.updatedTime
                    }

                    else -> {
                        // 没有 STOP，兜底用最后一条时间
                        records.last().updatedTime - firstStart.updatedTime
                    }
                }

                durationMs?.takeIf { it > 0L }?.let {
                    val minutes = it / 1000 / 60F
                    Timber.d(
                        "   [APP_SESSION][id=$id] duration=${minutes} minutes"
                    )
                }
            }
        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data?.toString() ?: return

        if (linkHandler.processLink(data)) {
            Timber.i("handleIntent Handled deep link: $data")
        } else {
            Timber.w("handleIntent Unhandled deep link: $data")
        }
    }
}
