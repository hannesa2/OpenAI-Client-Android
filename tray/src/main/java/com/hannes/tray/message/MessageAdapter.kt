package com.hannes.tray.message

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hannes.tray.GlideApp
import com.hannes.tray.R

class MessageAdapter(
    context: Context,
    private val onClick: ((text: String) -> String)? = null,
    private val onLongClick: ((text: String) -> String)? = null
) : ListAdapter<TrayMessage, RecyclerView.ViewHolder>(TrayMessage.DIFF_UTIL_CALLBACK) {

    private var startPosition = -1
    private val bubbleAnimation = AnimationUtils.loadAnimation(context, R.anim.item_enter)
    var onImageLoad: ((List<TrayMessage>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val msgView: LinearLayout

        val isSystem = TrayType.entries[viewType]
        msgView = when (isSystem) {
            TrayType.AI -> LayoutInflater.from(parent.context).inflate(R.layout.ai_msg_view, parent, false) as LinearLayout
            TrayType.USER -> LayoutInflater.from(parent.context).inflate(R.layout.user_msg_view, parent, false) as LinearLayout
            TrayType.ERROR -> LayoutInflater.from(parent.context).inflate(R.layout.ai_msg_error_view, parent, false) as LinearLayout
        }

        return BubbleViewHolder(msgView)
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return currentList[position].trayType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val message = currentList[position]

        val layout = (holder as BubbleViewHolder).msgLayout
        if (position > startPosition) {
            layout.startAnimation(bubbleAnimation)
            startPosition = position
        }

        val textView = layout.findViewById<TextView>(R.id.messageContent)
        if (message.imageURL.isNotEmpty()) {
            val imageUri = Uri.parse(message.imageURL)
            GlideApp.with(holder.itemView).load(imageUri).into(MessageBubbleTarget(textView))
        } else {
            textView.setCompoundDrawables(null, null, null, null)
        }
        textView.text = message.content
        textView.setOnClickListener {
            onClick?.invoke(textView.text.toString())
        }
        textView.setOnLongClickListener {
            onLongClick?.let {
                it.invoke(textView.text.toString())
                true
            } ?: run {
                false
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as BubbleViewHolder).msgLayout.clearAnimation()
    }

    class BubbleViewHolder(val msgLayout: LinearLayout) : RecyclerView.ViewHolder(msgLayout)

    inner class MessageBubbleTarget(private val textView: TextView) :
        CustomTarget<Drawable>(SIZE_ORIGINAL, SIZE_ORIGINAL) {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resource)
            onImageLoad?.invoke(currentList)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                placeholder
            )
        }
    }
}
