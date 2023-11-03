package com.mardillu.openai.message

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * A simple data class that describes the state necessary to provide a seamless transition
 * across app lifecycle events.
 */
data class TrayState(
    var isOpen: Boolean = false,
    var isActive: Boolean = false,
    var playTts: Boolean = true,
    var firstOpen: Boolean = true,
    var expectFollowup: Boolean = false,
    var messageStreamHeight: Int = 0,
    val messages: ArrayList<TrayMessage> = ArrayList()
) : Parcelable {

    private val messageData: MutableLiveData<ArrayList<TrayMessage>> = MutableLiveData(messages)

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readArrayList(ClassLoader.getSystemClassLoader()) as ArrayList<TrayMessage>
    )

    fun liveData(): LiveData<ArrayList<TrayMessage>> {
        return messageData
    }

    /**
     * Clear the conversation state without resetting any user modifications
     * like muting TTS or changing the tray size.
     */
    fun clear() {
        apply {
            expectFollowup = false
            firstOpen = true
            messages.clear()
            messageData.notifyObserver()
        }
    }

    /**
     * Overwrites the current tray state with a previously saved version.
     *
     * @param other A saved version of the tray state to load.
     */
    fun loadFrom(other: TrayState) {
        apply {
            isOpen = other.isOpen
            isActive = other.isActive
            playTts = other.playTts
            firstOpen = other.firstOpen
            expectFollowup = other.expectFollowup
            messageStreamHeight = other.messageStreamHeight
            messages.clear()
            messages.addAll(other.messages)
        }
    }

    fun addMessage(trayMessage: TrayMessage) {
        // observers only need to know when to add a message to the chat stream;
        // other data is only for saving/restoring UI state
        messages.add((trayMessage))
        messageData.notifyObserver()
    }

    fun addOrUpdateUserMessage(text: String) {
        val message = messages.lastOrNull()
        if (message == null || message.isSystem) {
            addMessage(TrayMessage(content = text))
        } else {
            messages[messages.size - 1] = TrayMessage(false, text)
            messageData.notifyObserver()
        }
    }

    override fun describeContents(): Int {
        // unnecessary
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (isOpen) 1 else 0)
        dest.writeByte(if (isActive) 1 else 0)
        dest.writeByte(if (playTts) 1 else 0)
        dest.writeByte(if (firstOpen) 1 else 0)
        dest.writeByte(if (expectFollowup) 1 else 0)
        dest.writeInt(messageStreamHeight)
        dest.writeList(messages as List<TrayMessage>)
    }

    companion object CREATOR : Parcelable.Creator<TrayState> {
        override fun createFromParcel(parcel: Parcel): TrayState {
            return TrayState(parcel)
        }

        override fun newArray(size: Int): Array<TrayState?> {
            return arrayOfNulls(size)
        }

    }
}

// the LiveData's value must be set (hence incrementing its version) for observers to be updated
// see https://stackoverflow.com/a/52075248/421784
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}
