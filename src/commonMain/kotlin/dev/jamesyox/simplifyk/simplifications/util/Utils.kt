package dev.jamesyox.simplifyk.simplifications.util

@PublishedApi
internal inline fun <T> T.coord(
    crossinline xExtractor: (T) -> Double,
    crossinline xTransformer: (Double) -> Double
): Double {
    return xTransformer(xExtractor(this))
}
