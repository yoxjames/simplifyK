package dev.jamesyox.simplifyk

import dev.jamesyox.simplifyk.simplifications.radialDistance
import dev.jamesyox.simplifyk.simplifications.ramerDouglasPeucker

/**
 * For a List<T> returns a List<T> of simplified points. This is a direct port of simplify-js and should
 * function the same.
 *
 * For more information on simplify-js see [simplify-js](http://mourner.github.io/simplify-js/)
 *
 * @receiver A List<T> representing two-dimensional points that you wish to simplify
 *
 * @param T The type of the data points you wish to simplify
 * @param tolerance Affects the amount of simplification
 * @param highestQuality: Setting to true will exclude Radial Distance based preprocessing but may take longer.
 * @param xExtractor A function that given an input T can extract the x value
 * @param yExtractor A function that given an input T can extract the y value
 * @param xTransformer Optional function that transforms the x value sent to the simplification algorithms
 *                      but not what is returned by the simplification.
 * @param yTransformer Optional function that transforms the y value sent to the simplification algorithms
 *                      but not what is returned by the simplification.
 */
@Suppress("LongParameterList")
public inline fun <T>List<T>.simplify(
    tolerance: Double = 1.0,
    highestQuality: Boolean = false,
    crossinline xExtractor: (T) -> Double,
    crossinline yExtractor: (T) -> Double,
    crossinline xTransformer: (Double) -> Double = { it },
    crossinline yTransformer: (Double) -> Double = { it }
): List<T> {
    val sqTolerance = tolerance * tolerance
    return when (highestQuality) {
        true -> ramerDouglasPeucker(
            epsilon = sqTolerance,
            xExtractor = xExtractor,
            yExtractor = yExtractor,
            xTransformer = xTransformer,
            yTransformer = yTransformer
        )
        false -> radialDistance(
            tolerance = sqTolerance,
            xExtractor = xExtractor,
            yExtractor = yExtractor,
            xTransformer = xTransformer,
            yTransformer = yTransformer
        ).ramerDouglasPeucker(
            epsilon = sqTolerance,
            xExtractor = xExtractor,
            yExtractor = yExtractor,
            xTransformer = xTransformer,
            yTransformer = yTransformer
        )
    }
}

/**
 * For a List of Pair<Double, Double> returns a List<Double, Double> representing the simplified polyline.
 * It is strongly encouraged to use [simplify] on your own data types with your own xExtractor and yExtractor
 * to avoid making unnecessary copies of large data sets.
 *
 * @see [simplify]
 */
public fun List<Pair<Double, Double>>.simplify(
    tolerance: Double = 1.0,
    highestQuality: Boolean = false
): List<Pair<Double, Double>> {
    return simplify(
        tolerance = tolerance,
        highestQuality = highestQuality,
        xExtractor = { it.first },
        yExtractor = { it.second }
    )
}
