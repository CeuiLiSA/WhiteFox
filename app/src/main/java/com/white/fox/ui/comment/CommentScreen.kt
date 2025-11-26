package com.white.fox.ui.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.PagedListScreen
import com.white.fox.ui.common.constructKeyedVM

@Composable
fun CommentScreen(objectId: Long, objectType: String) = PageScreen("Comments") {
    val api = LocalDependency.current.client.appApi
    val viewModel = constructKeyedVM(
        { "comment-${objectId}-${objectType}" },
        { objectId to objectType }) { (id, type) ->
        CommentViewModel(id, type, api)
    }

    val childReplies by viewModel.childComments.collectAsState()

    PagedListScreen(viewModel) { comment ->
        CommentItem(comment, childComments = childReplies[comment.id].orEmpty()) {
            viewModel.showReplies(comment.id)
        }
    }
}