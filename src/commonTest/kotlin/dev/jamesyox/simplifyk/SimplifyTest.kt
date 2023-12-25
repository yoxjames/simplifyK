package dev.jamesyox.simplifyk

import kotlin.test.Test
import kotlin.test.assertEquals

class SimplifyTest {
    @Test
    fun FullDatasetTest() {
        assertEquals(expected = simplified, actual = points.simplify(5.0))
    }

    @Test
    fun NoChangeIfOnlyOnePoint() {
        val input = listOf(Point2D(1.0, 2.0))
        assertEquals(expected = input, actual = input.simplify())
    }

    @Test
    fun ReturnEmptyWhenInputEmpty() {
        assertEquals(expected = emptyList(), actual = emptyList<Point2D>().simplify())
    }
}