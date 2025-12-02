package ceui.lisa.hermes.task

import ceui.lisa.hermes.loadstate.LoadReason
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class SlideShowQueue(
    private val intervalMillis: Long = 6000L,
    private val preloadCount: Int = 2
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val tasks = mutableListOf<ImageLoaderTask>()
    private val _currentImage = MutableStateFlow<File?>(null)
    val currentImage: StateFlow<File?> = _currentImage

    private var index = 0

    fun submitTasks(taskList: List<ImageLoaderTask>) {
        tasks.clear()
        tasks.addAll(taskList)
    }

    fun start() {
        scope.launch {
            while (tasks.isNotEmpty()) {
                displayCurrent()
                preloadNext()
                delay(intervalMillis)
                next()
            }
        }
    }

    private suspend fun displayCurrent() {
        val task = tasks[index]
        // 当前图片按需下载
        task.runTask(LoadReason.InitialLoad)
        _currentImage.value = task.valueFlow.value
    }

    private suspend fun preloadNext() {
        repeat(preloadCount) { offset ->
            val nextIndex = (index + offset + 1) % tasks.size
            val task = tasks[nextIndex]
            task.runTask(LoadReason.InitialLoad)
        }
    }

    fun next() {
        index = (index + 1) % tasks.size
    }

    fun previous() {
        index = if (index - 1 < 0) tasks.size - 1 else index - 1
    }
}
