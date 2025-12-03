package ceui.lisa.hermes.task

import ceui.lisa.hermes.loadstate.LoadReason
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class SlideShowQueue(
    private val intervalMillis: Long = 8000L,
    private val preloadCount: Int = 2
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val tasks = mutableListOf<ImageLoaderTask>()
    private val _currentImage = MutableStateFlow<File?>(null)
    val currentImage: StateFlow<File?> = _currentImage

    // 新增可观察的索引和总数
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _totalSize = MutableStateFlow(0)
    val totalSize: StateFlow<Int> = _totalSize.asStateFlow()

    private var index = 0

    fun submitTasks(taskList: List<ImageLoaderTask>) {
        tasks.clear()
        tasks.addAll(taskList)
        _totalSize.value = tasks.size
        index = 0
        _currentIndex.value = index
    }

    fun appendTasks(newTasks: List<ImageLoaderTask>) {
        if (newTasks.isEmpty()) return

        tasks.addAll(newTasks)
        _totalSize.value = tasks.size
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
        task.runTask(LoadReason.InitialLoad)
        _currentImage.value = task.valueFlow.value
        _currentIndex.value = index
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
        _currentIndex.value = index
    }

    fun previous() {
        index = if (index - 1 < 0) tasks.size - 1 else index - 1
        _currentIndex.value = index
    }
}
