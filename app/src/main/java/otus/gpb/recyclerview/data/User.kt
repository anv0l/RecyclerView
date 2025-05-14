package otus.gpb.recyclerview.data

import java.time.Instant

data class User(
    val id: Int,
    val name: String,
    val nickName: String = "",         // I have no clue what this line actually is
    val avatar: String,                // Or Int? Some sort of a reference to an image
    val lastSeen: Instant,
    val isVerified: Boolean = false,
    val statusMessage: String? = null    // text of the status that was set by the user
)