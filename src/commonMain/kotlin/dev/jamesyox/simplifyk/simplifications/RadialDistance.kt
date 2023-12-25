package dev.jamesyox.simplifyk.simplifications

import dev.jamesyox.simplifyk.simplifications.util.coord

/**
 * Takes a list of objects representing a polyline with a xExtractor and yExtractor function and applies
 * the Radial Distance (RD) algorithm returning the resulting list. This algorithm goes through each point and
 * prunes points that are within the radial distance represented by tolerance param reducing the
 * total number of points.
 *
 * @receiver A [List] of any data type T that you wish to apply the RD algorithm to.
 *
 * @param T The type of the data points you wish to simplify
 * @param tolerance The max radial distance from each "key" value that can be removed from the resulting polyline.
 * @param xExtractor A function that given an input T can extract the x value
 * @param yExtractor A function that given an input T can extract the y value
 * @param xTransformer Optional function that transforms the x value sent to the simplification algorithms
 *                      but not what is returned by the simplification.
 * @param yTransformer Optional function that transforms the y value sent to the simplification algorithms
 *                      but not what is returned by the simplification.
 *
 * @return A List<T> which represents the receiver with the RD algorithm applied
 */
public inline fun <T>List<T>.radialDistance(
    tolerance: Double,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double,
    crossinline xTransformer: (Double) -> Double = { it },
    crossinline yTransformer: (Double) -> Double = { it },
): List<T> {
    if (size <= 2) return this
    var prevPoint = first()
    val newPoints = mutableListOf(prevPoint)
    for (i in 1 until size) {
        val point = this[i]

        if (getSquareDistance(
                p1 = point,
                p2 = prevPoint,
                xExtractor = xExtractor,
                yExtractor = yExtractor,
                xTransformer = xTransformer,
                yTransformer = yTransformer
            ) > tolerance
        ) {
            newPoints.add(point)
            prevPoint = point
        }
    }

    if (prevPoint != last()) newPoints.add(last())
    return newPoints.toList()
}

@PublishedApi
@Suppress("LongParameterList")
internal inline fun <T>getSquareDistance(
    p1: T,
    p2: T,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double,
    crossinline xTransformer: (Double) -> Double,
    crossinline yTransformer: (Double) -> Double
): Double {
    val dx = p1.coord(xExtractor, xTransformer) - p2.coord(xExtractor, xTransformer)
    val dy = p1.coord(yExtractor, yTransformer) - p2.coord(yExtractor, yTransformer)
    return dx * dx + dy * dy
}
