package com.white.fox.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ceui.lisa.hermes.loader.APIRepository
import ceui.lisa.hermes.loadstate.LoadMoreOwner
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import ceui.lisa.hermes.objectpool.ObjectPool
import ceui.lisa.models.Comment
import ceui.lisa.models.CommentResponse
import ceui.lisa.models.ObjectType
import com.white.fox.client.AppApi
import com.white.fox.client.ListValueContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class CommentViewModel(
    private val objectId: Long,
    private val objectType: String,
    private val appApi: AppApi,
) : ViewModel(), RefreshOwner<CommentResponse>, LoadMoreOwner {


    override fun refresh(reason: LoadReason) = valueContent.refresh(reason)

    private val valueContent =
        ListValueContent(
            viewModelScope,
            APIRepository {
                if (objectType == ObjectType.NOVEL) {
                    appApi.getNovelComments(objectId)
                } else {
                    appApi.getIllustComments(objectId)
                }
            },
            appApi,
            sum = { old, new ->
                new.copy(comments = old.comments + new.comments)
            }) { response ->
            response.displayList.forEach { comment ->
                comment.user.let { user ->
                    ObjectPool.update(user)
                }
            }
        }

    override fun loadNextPage() {
        valueContent.loadNextPage()
    }

    override val loadState: StateFlow<LoadState> = valueContent.loadState
    override val valueFlow: StateFlow<CommentResponse?> = valueContent.totalFlow

    private val _childComments =
        MutableStateFlow<Map<Long, List<Comment>>>(emptyMap())
    val childComments: StateFlow<Map<Long, List<Comment>>> = _childComments

    fun showReplies(commentId: Long) {
        viewModelScope.launch {
            try {
                val resp = appApi.getIllustReplyComments(objectType, commentId)
                _childComments.update { old ->
                    old + (commentId to resp.comments)
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun reply(commentId: Long) {
        viewModelScope.launch {
            try {

            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}