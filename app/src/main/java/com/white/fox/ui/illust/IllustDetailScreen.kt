package com.white.fox.ui.illust

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Illust
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import timber.log.Timber

@Composable
fun IllustDetailScreen(illustId: Long, navViewModel: NavViewModel) {


    val illustLiveData = ObjectPool.get<Illust>(illustId)

    val illust = illustLiveData.observeAsState().value

    SideEffect {
        Timber.d("IllustDetailScreen recomposed ${illust?.title}")
    }


    ContentTemplate("IllustDetailScreen") {
        if (illust == null) {
            CircularProgressIndicator()
        } else {
            Text("Title: ${illust.title}")
            Text("ID: ${illust.id}")


            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val oldName = illust.title
                ObjectPool.update(illust.copy(title = "${oldName}-${System.currentTimeMillis()}"))
            }) {
                Text("点击按钮")
            }

        }
    }

}