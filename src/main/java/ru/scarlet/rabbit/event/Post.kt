package ru.scarlet.rabbit.event

import java.util.UUID

data class Post(
    var id: UUID? = UUID.randomUUID(),
    var title: String? = null,
    var content: String? = null,
    var author: String? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    val body: String? = null,
) {

}

data class Comment
    (
    var id: UUID? = UUID.randomUUID(),
    var postId: UUID? = null,
    var content: String? = null,
    var author: String? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    val body: String? = null
)