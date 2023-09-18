# SimplifyK

This is a small Kotlin Multiplatform library for polyline simplification. It is essentially a port of [simplify-js](https://mourner.github.io/simplify-js/) in common Kotlin.

## Quick Start
The most simply use case closely mirrors the API of simplify-js. However, this library contains no concrete implementation of a Point or Point2D data type. It is expected that callers will have their own implementation. Rather than have callers implement an interface or convert a list to a different data type, which could be expensive or impractical on a large data, callers are expected to provide an `xExtractor` and `yExtractor` telling the algorithm how to get the `x` and `y` coordinates.

```kotlin

data class MyPoint(
    val x: Double,
    val y: Double
)

fun myFunction(pointsToSimplify: List<MyPoint>): List<MyPoint> {
    return pointsToSimplify.simplify(
        tolerance = 1.0,
        highestQuality = false,
        xExtractor = { it.x },
        yExtractor = { it.y }
    )
}
```

As you can see, a new list will be returned of your own data type `MyPoint` preventing the need to convert things back and forth.

Each simplification can also be called by itself. For instance this is also valid:

```kotlin

val largeDataset: List<MyPoint> = TODO()

val simplifiedWithRDP = largeDataset.ramerDouglasPeucker(
    epsilon = 0.1,
    xExtractor = { it.x },
    yExtractor = { it.y }
)

val simplifiedWithRD = largeDataset.radialDistance(
    tolerance = 0.1,
    xExtractor = { it.x },
    yExtractor = { it.y }
)
```

## What is it
SimplifyK implements two polyline simplification algorithms. Radial Distance (RD) and Ramer-Douglas-Peucker (RDP). Both algoritms are designed to take points in a polyline and reduce the number of points while retaining the same general shape. This is used frequently for charting large datasets where many points would lead to performance issues and visual noise.

Simplify-js applies Radial Distance first which is an algorithm that crawls along a polyline eliminating any points within the radial distance passed in. The result of that is then applied to Ramer-Douglas-Peucker for further simplification. Radial Distance is done first because it is faster than Ramer-Douglas-Peucker.

## Implementation Details
Ramer-Douglas-Peucker is a recursive algorithm. To prevent any stack overflow issues `DeepRecursiveFunction` (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-deep-recursive-function/) from the Kotlin standard library was used. Therefore, this should work on even large non-optimal data sets. 

## Contributing
Contributions are welcome including additional polyline simplification algorithms and performance improvements. Before opening a pr be sure to run tests `./gradlew check` for all platforms and linting and static analysis `./gradlew detekt`. This project also includes some JVM benchmarks which can be run with `./gradlew bench` if you have performance improvements to propose I would appreciate including benchmarks for each implementation.