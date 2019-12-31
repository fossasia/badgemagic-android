package org.fossasia.badgemagic.data

import kotlinx.serialization.Serializable

@Serializable
data class DataToSend(val messages: List<Message>)
