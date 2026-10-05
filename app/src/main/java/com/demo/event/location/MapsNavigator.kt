package com.demo.event.location

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

object MapsNavigator {

    fun openDirections(
        context: Context,
        latitude: Double,
        longitude: Double,
        title: String
    ) {
        val encodedTitle = Uri.encode(title)
        val uriString = "geo:$latitude,$longitude?q=$latitude,$longitude($encodedTitle)"
        val uri = uriString.toUri()

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                val fallbackUri = "geo:$latitude,$longitude?q=$latitude,$longitude($encodedTitle)".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, fallbackUri))
            }
        } catch (_: Exception) {
            val webUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }
}
