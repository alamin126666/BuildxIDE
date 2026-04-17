package com.buildxide.util

import android.util.Base64

object Base64Util {

    fun encodeToString(input: String): String {
        return Base64.encodeToString(input.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    fun decodeToString(input: String): String {
        return String(Base64.decode(input, Base64.DEFAULT), Charsets.UTF_8)
    }

    fun encodeToBytes(input: String): ByteArray {
        return Base64.encode(input.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    fun decodeToBytes(input: String): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }
}
