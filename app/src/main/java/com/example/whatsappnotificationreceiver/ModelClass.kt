package com.example.whatsappnotificationreceiver

import android.graphics.Bitmap

class ModelClass(var from: String, var msg: String, var isImage: Boolean, var img: Bitmap?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModelClass

        if (from != other.from) return false
        if (msg != other.msg) return false

        return true
    }

    override fun hashCode(): Int {
        var result = from.hashCode()
        result = 31 * result + msg.hashCode()
        return result
    }
}