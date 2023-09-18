package dev.yoxjames.simplifyk.simplifications

/**
 * Takes a list of objects representing a polyline with a xExtractor and yExtractor function and applies
 * the Radial Distance (RD) algorithm returning the resulting list. This algorithm goes through each point and
 * prunes points that are within the radial distance represented by @param[tolerance] reducing the
 * total number of points.
 *
 * @receiver A list of any data type that you wish to apply the RD algorithm to.
 *
 * @param tolerance The max radial distance from each "key" value that can be removed from the resulting polyline.
 * @param xExtractor A function that given an input T can extract the x value
 * @param yExtractor A function that given an input T can extract the y value
 *
 * @return A List<T> which represents the receiver with the RD algorithm applied
 */
public inline fun <T>List<T>.radialDistance(
    tolerance: Double,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double
): List<T> {
    if (size <= 2) return this
    var prevPoint = first()
    val newPoints = mutableListOf(prevPoint)
    for (i in 1 until size) {
        val point = this[i]

        if (getSquareDistance(point, prevPoint, xExtractor, yExtractor) > tolerance) {
            newPoints.add(point)
            prevPoint = point
        }
    }

    if (prevPoint != last()) newPoints.add(last())
    return newPoints.toList()
}

@PublishedApi
internal inline fun <T>getSquareDistance(
    p1: T,
    p2: T,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double
): Double {
    val dx = xExtractor(p1) - xExtractor(p2)
    val dy = yExtractor(p1) - yExtractor(p2)
    return dx * dx + dy * dy
}
