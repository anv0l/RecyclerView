package otus.gpb.recyclerview.data

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

enum class MessageStatus {
    READ, DELIVERED
}

enum class MessageDirection {
    IN, OUT
}

data class Chat(
    var id: Int,
    val user: User,                          // user with whom the chat is held
    val timeRead: Instant? = null,           // Time when potentially the chat was seen/"read" status
    val isScam: Boolean = false,             // Automatically (or manually) marked as spam
    val mutedUntil: Instant? = null,         // Time until chat is muted, i.e. you won't get notifications from the chat;
    val isMutedForever: Boolean = false,
    var isArchived: Boolean,                 // The chat is archived, it won't be displayed in the main chat list
    var isVoip: Boolean = false,             // Has voice message (?)
    var timeLast: Instant? = null,
    var textLast: String = "",
    var isMentionedLast: Boolean = false,
    var statusLast: MessageStatus? = null,
    var unreadCount: Int = 0,
    var directionLast: MessageDirection? = null     // for displaying checkers in front of the last message date

) {
    fun isMuted(): Boolean {
        return isMutedForever || (mutedUntil != null && mutedUntil.isAfter(Instant.now()))
    }

    fun getLastMessageTime(): String {
        val result: String =
            if (timeLast == null)
                ""
            else {
                val diff = Duration.between(timeLast, Instant.now()).abs()
                val dateTimeWithTimeZone = timeLast!!.atZone(ZoneId.systemDefault())
                DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault())
                when {
                    diff.toHours() < 24 -> {
                        DateTimeFormatter.ofPattern("HH:mm").format(dateTimeWithTimeZone)
                    }

                    diff.toDays() < 7 -> {
                        DateTimeFormatter.ofPattern("EEE").format(dateTimeWithTimeZone)
                    }

                    diff.toDays() < 365 -> {
                        DateTimeFormatter.ofPattern("MMM dd").format(dateTimeWithTimeZone)
                    }

                    else -> {
                        DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dateTimeWithTimeZone)
                    }
                }
            }
        return result
    }

}