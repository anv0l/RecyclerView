package otus.gpb.recyclerview.ui

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import otus.gpb.recyclerview.R
import otus.gpb.recyclerview.data.Chat
import otus.gpb.recyclerview.data.MessageDirection
import otus.gpb.recyclerview.data.MessageStatus
import otus.gpb.recyclerview.databinding.VhChatsBinding
import kotlin.math.abs

class ChatViewHolder(private val binding: VhChatsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var id: Int = -1

    private fun Boolean.setViewVisibility(): Int {
        return if (this) View.VISIBLE else View.GONE
    }

    private fun Chat.bindUserInfo(binding: VhChatsBinding) {
        with(binding) {
            txtUserName.text = user.name
            if (user.nickName == "")
                txtUserName2.visibility = View.GONE
            else {
                txtUserName2.visibility = View.VISIBLE
                txtUserName2.text = user.nickName
            }
            Glide.with(itemView.context).load(user.avatar).centerCrop()
                .into(imgUserAvatar)
            imgUserVerified.visibility = user.isVerified.setViewVisibility()
        }
    }

    private fun Chat.bindMessageInfo(binding: VhChatsBinding) {
        with(binding) {
            txtLastMessageTime.text = getLastMessageTime()
            txtLastMessage.text = textLast

            imgIsRead.visibility =
                (statusLast == MessageStatus.READ && directionLast == MessageDirection.OUT).setViewVisibility()
            imgIsSent.visibility = (statusLast == MessageStatus.DELIVERED && directionLast == MessageDirection.OUT).setViewVisibility()

            imgIsMuted.visibility = isMuted().setViewVisibility()
            if (isMentionedLast || unreadCount == 0) {
                imgBadgeRightContainer.visibility = View.GONE
            } else {
                imgBadgeRightContainer.visibility = View.VISIBLE
                txtBadgeRight.text =
                    if (isMentionedLast) "@" else unreadCount.toString()
            }
            imgUserAvatarVoipContainer.visibility = when (isVoip) {
                true -> View.VISIBLE
                else -> View.GONE
            }
            imgLastMessageAttachment.visibility = View.GONE
            imgIsScam.visibility = if (isScam) View.VISIBLE else View.GONE
        }
    }

    fun bind(chat: Chat) {
        with(binding) {
            this@ChatViewHolder.id = chat.id
            chat.bindUserInfo(binding)
            chat.bindMessageInfo(binding)

            swipeRevealContainer.visibility = View.INVISIBLE
            container.translationX = 0f
        }
    }

    fun updateSwipeState(dX: Float) {
        binding.container.translationX = dX

        val swipeThreshold = binding.swipeRevealContainer.width / 4

        when {
            (abs(dX) > swipeThreshold) ->
                binding.swipeRevealContainer.setBackgroundColor(Color.GRAY)

            else ->
                binding.swipeRevealContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.color_accent_main
                    )
                )
        }

        binding.swipeRevealContainer.translationX = -dX
        binding.swipeRevealContainer.visibility = when {
            abs(dX) > 5f -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    fun resetSwipeState() {
        binding.container.translationX = 0f
        binding.guidelineRevealContainer.visibility = View.INVISIBLE
    }

}