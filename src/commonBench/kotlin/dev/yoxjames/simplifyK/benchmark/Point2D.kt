package dev.yoxjames.simplifyK.benchmark

import dev.yoxjames.simplifyk.simplify

data class Point2D(
    val x: Double,
    val y: Double,
)

fun List<Point2D>.simplify(tolerance: Double = 1.0, highestQuality: Boolean = false): List<Point2D> {
    return simplify(
        tolerance = tolerance,
        highestQuality = highestQuality,
        xExtractor = { it.x },
        yExtractor = { it.y }
    )
}
