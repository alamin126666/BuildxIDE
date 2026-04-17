package com.buildxide.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

// String extensions
fun String?.orDefault(default: String = ""): String = this ?: default

fun String.truncate(maxLength: Int, suffix: String = "..."): String {
    return if (length > maxLength) {
        substring(0, maxLength - suffix.length) + suffix
    } else {
        this
    }
}

fun String.toSlug(): String {
    return this.lowercase()
        .replace("[^a-z0-9\\s-]".toRegex(), "")
        .replace("\\s+".toRegex(), "-")
        .replace("-+", "-")
        .trim('-')
}

// Long extensions (timestamps)
fun Long.formatAsDate(pattern: String = "MMM d, yyyy HH:mm"): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}

fun Long.timeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "just now"
        diff < TimeUnit.HOURS.toMillis(1) -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
        }
        diff < TimeUnit.DAYS.toMillis(1) -> {
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            "$hours ${if (hours == 1L) "hour" else "hours"} ago"
        }
        diff < TimeUnit.DAYS.toMillis(7) -> {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            "$days ${if (days == 1L) "day" else "days"} ago"
        }
        diff < TimeUnit.DAYS.toMillis(30) -> {
            val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
            "$weeks ${if (weeks == 1L) "week" else "weeks"} ago"
        }
        else -> {
            val date = Date(this)
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }
}

// Int extensions
fun Int.toByteSize(): String {
    val bytes = this.toLong()
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}

fun Long.toByteSize(): String {
    return when {
        this < 1024 -> "$this B"
        this < 1024 * 1024 -> "${this / 1024} KB"
        this < 1024 * 1024 * 1024 -> "${this / (1024 * 1024)} MB"
        else -> "${this / (1024 * 1024 * 1024)} GB"
    }
}

// Boolean extensions
fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true

// List extensions
fun <T> List<T>?.orEmptyList(): List<T> = this ?: emptyList()

fun <T> List<T>.replace(old: T, new: T): List<T> {
    return map { if (it == old) new else it }
}

fun <T> List<T>.toggle(item: T): List<T> {
    return if (contains(item)) {
        filter { it != item }
    } else {
        this + item
    }
}
