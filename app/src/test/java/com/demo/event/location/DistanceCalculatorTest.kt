package com.demo.event.location

import org.junit.Assert.assertEquals
import org.junit.Test

class DistanceCalculatorTest {

    @Test
    fun formatDistance_lessThan1000_returnsMeters() {
        val result = DistanceCalculator.formatDistance(500f)
        assertEquals("500 m", result)
    }

    @Test
    fun formatDistance_1000OrMore_returnsKilometers() {
        val result = DistanceCalculator.formatDistance(1500f)
        assertEquals("1.5 km", result)
    }
}
