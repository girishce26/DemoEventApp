package com.demo.event.location

import android.location.Location

object DistanceCalculator {

    fun calculateDistance(
        userLatitude: Double,
        userLongitude: Double,
        eventLatitude: Double,
        eventLongitude: Double
    ): Float {

        val result = FloatArray(1)

        Location.distanceBetween(
            userLatitude,
            userLongitude,
            eventLatitude,
            eventLongitude,
            result
        )

        return result[0]
    }

    fun formatDistance(
        distanceMeters: Float
    ): String {

        return if (distanceMeters < 1000) {

            "${distanceMeters.toInt()} m"

        } else {

            "%.1f km".format(
                distanceMeters / 1000f
            )
        }
    }
}