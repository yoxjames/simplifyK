package dev.yoxjames.simplifyk.simplifications

/**
 * Takes a list of objects representing a polyline with a xExtractor and yExtractor functions and applies
 * the Ramer-Douglas-Peucker (RDP) algorithm returning the resulting list.
 *
 * @receiver A list of any data type that you wish to apply the Ramer-Douglas-Peucker algorithm to.
 *
 * @param epsilon The epsilon in the RDP algorithm
 * @param xExtractor A function that given an input T can extract the x value
 * @param yExtractor A function that given an input T can extract the y value
 *
 * @return A List<T> which represents the receiver with the RDP algorithm applied
 */
public inline fun <T>List<T>.ramerDouglasPeucker(
    epsilon: Double,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double
): List<T> {
    if (size <= 2) return this
    val simplified = mutableListOf(first())
    getSimplifyDpStep(epsilon, xExtractor, yExtractor).invoke(
        DpStepParams(this, 0, lastIndex, epsilon, simplified)
    )
    simplified.add(last())
    return simplified.toList()
}

@PublishedApi
internal data class DpStepParams<T>(
    val points: List<T>,
    val firstIndex: Int,
    val lastIndex: Int,
    val sqTolerance: Double,
    val simplified: MutableList<T>
)

@PublishedApi
internal inline fun <T> getSimplifyDpStep(
    sqTolerance: Double,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double
): DeepRecursiveFunction<DpStepParams<T>, Unit> {
    return DeepRecursiveFunction { params ->
        var maxSqDist = sqTolerance
        var index = 0

        for (i in (params.firstIndex + 1) until params.lastIndex) {
            val sqDist = getSquareSegDistance(
                p = params.points[i],
                p1 = params.points[params.firstIndex],
                p2 = params.points[params.lastIndex],
                xExtractor = xExtractor,
                yExtractor = yExtractor
            )
            if (sqDist > maxSqDist) {
                index = i
                maxSqDist = sqDist
            }
        }
        if (maxSqDist > sqTolerance) {
            if ((index - params.firstIndex) > 1) {
                callRecursive(
                    DpStepParams(params.points, params.firstIndex, index, sqTolerance, params.simplified)
                )
            }
            params.simplified.add(params.points[index])
            if ((params.lastIndex - index) > 1) {
                callRecursive(
                    DpStepParams(params.points, index, params.lastIndex, sqTolerance, params.simplified)
                )
            }
        }
    }
}

@PublishedApi
internal inline fun <T>getSquareSegDistance(
    p: T,
    p1: T,
    p2: T,
    xExtractor: (T) -> Double,
    yExtractor: (T) -> Double
): Double {
    var x = xExtractor(p1)
    var y = yExtractor(p1)
    var dx = xExtractor(p2) - x
    var dy = yExtractor(p2) - y

    if (dx != 0.0 || dy != 0.0) {
        val t = ((xExtractor(p) - x) * dx + (yExtractor(p) - y) * dy) / (dx * dx + dy * dy)

        if (t > 1.0) {
            x = xExtractor(p2)
            y = yExtractor(p2)
        } else if (t > 0.0) {
            x += dx * t
            y += dy * t
        }
    }

    dx = xExtractor(p) - x
    dy = yExtractor(p) - y

    return dx * dx + dy * dy
}
