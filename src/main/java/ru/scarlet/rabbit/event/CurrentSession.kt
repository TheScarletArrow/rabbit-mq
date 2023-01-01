package ru.scarlet.rabbit.event

import java.util.UUID

data class CurrentSession(
    val sessionId: UUID,
    val sessionName: String,
    val customerId: UUID,
    val startedAt: Long,
    val isActive: Boolean = true
)