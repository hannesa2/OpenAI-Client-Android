package com.mardillu.openai.message

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil

/**
 * A simple data class that describes a message in Spokestack. Messages have text or image contents and can
 * be initiated by the user or the system.
 */
data class TrayMessage(val isSystem: Boolean = false, val content: String, val imageURL: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    companion object CREATOR : Parcelable.Creator<TrayMessage> {
        override fun createFromParcel(parcel: Parcel): TrayMessage {
            return TrayMessage(parcel)
        }

        override fun newArray(size: Int): Array<TrayMessage?> {
            return arrayOfNulls(size)
        }

        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<TrayMessage>() {
            override fun areItemsTheSame(oldItem: TrayMessage, newItem: TrayMessage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TrayMessage, newItem: TrayMessage): Boolean {
                return oldItem.content == newItem.content
                        && oldItem.imageURL == newItem.imageURL
            }

        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrayMessage

        if (isSystem != other.isSystem) return false
        if (content != other.content) return false
        if (imageURL != other.imageURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isSystem.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + imageURL.hashCode()
        return result
    }

    override fun describeContents(): Int {
        // unnecessary
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (isSystem) 1 else 0)
        dest.writeString(content)
        dest.writeString(imageURL)
    }
}
