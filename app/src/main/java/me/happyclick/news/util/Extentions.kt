package me.happyclick.news.util

import android.content.Context
import android.text.format.DateUtils
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import me.happyclick.news.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_movie_icon)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

@BindingAdapter("android:formattedDateTime")
fun formatDateTime(view: AppCompatTextView, publishedAt: String?) {
    view.publishedAtTime(publishedAt)
}

fun AppCompatTextView.publishedAtTime(publishedAt: String?) {
    val localFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
    val enUsFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val iso8601Format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    var date: Date? = null
    synchronized(localFormat) {
        try {
            date = localFormat.parse(publishedAt)
        } catch (ignored: ParseException) { }
        try {
            date = enUsFormat.parse(publishedAt)
        } catch (ignored: ParseException) { }
        try {
            date = iso8601Format.parse(publishedAt)
        } catch (ignored: ParseException) { }
    }
    try {
        val formatted = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US).format(date!!)
        this.setText(formatted)
    } catch (e: ParseException) {
        e.printStackTrace()
        this.setText(publishedAt ?: "")
    }
}