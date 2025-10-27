package com.white.fox.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.white.fox.R
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() = PageScreen(pageTitle = localizedString(R.string.settings)) {
    val settingsManager = LocalDependency.current.settingsManager
    val settingsState = settingsManager.stateFlow.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = localizedString(R.string.settings_language),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppLanguage.entries.forEach { lang ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        settingsManager.updateFromPrev { settings ->
                            settings.copy(language = lang)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = lang == settingsState.value?.language,
                    onClick = {
                        settingsManager.updateFromPrev { settings ->
                            settings.copy(language = lang)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (lang) {
                        AppLanguage.SYSTEM -> "System"
                        AppLanguage.CHINESE -> stringResource(R.string.language_chinese)
                        AppLanguage.JAPANESE -> stringResource(R.string.language_japanese)
                        AppLanguage.KOREAN -> stringResource(R.string.language_korean)
                        AppLanguage.ENGLISH -> "English"
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
