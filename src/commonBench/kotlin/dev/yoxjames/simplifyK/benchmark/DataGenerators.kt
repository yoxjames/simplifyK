package dev.yoxjames.simplifyK.benchmark

import kotlin.math.sin

fun generateNoisySineWave(numPoints: Int, noiseMagnitude: Float): List<Point2D> {
    // Generate a simple linear path with noise
    return (0 until numPoints).map {
        val x = it.toDouble()
        val y = sin(x) + (Math.random() - 0.5) * noiseMagnitude
        Point2D(x, y)
    }
}
