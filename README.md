# SimplifyK

![Maven Central Version](https://img.shields.io/maven-central/v/dev.jamesyox/simplifyK)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.23-blue.svg?logo=kotlin)](http://kotlinlang.org)

This is a small Kotlin Multiplatform library for polyline simplification. It is essentially a port of [simplify-js](https://mourner.github.io/simplify-js/) in common Kotlin.

## Quick Start
This project is deployed to Maven Central. The following are the coordinates:

groupId: `dev.jamesyox`

artifactId: `simplifyK`

version: `0.2.0`

To include in a gradle based project you would add this to your dependencies block.

```kotlin
implementation("dev.jamesyox:simplifyK:0.2.0")
```

The primary use case closely mirrors the API of simplify-js. However, this library contains no concrete implementation of a Point or Point2D data type. It is expected that callers will have their own implementation. Rather than have callers implement an interface or convert a list to a different data type, which could be expensive or impractical on a large dataset, callers are expected to provide a `xExtractor` and `yExtractor` telling the algorithm how to get the `x` and `y` coordinates for a given type.

```kotlin
// Can be anything you want.
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
// Run the Ramer-Douglas-Peucker algorithm on a list of MyPoint returning the result
val simplifiedWithRDP = largeDataset.ramerDouglasPeucker(
    epsilon = 0.1,
    xExtractor = { it.x },
    yExtractor = { it.y }
)
// Run the Radial Distance algorithm on a list of MyPoint returning the result
val simplifiedWithRD = largeDataset.radialDistance(
    tolerance = 0.1,
    xExtractor = { it.x },
    yExtractor = { it.y }
)
```

## Value transformation
A common use case of polyline simplification is to simplify the number of points before rendering on a graph. We may have some data collected over time. On the X axis might be temperature, on the Y axis might be the time in milliseconds. Additionally, we may want to render a graph on different screen sizes. Perhaps the user can even resize the graph we are rendering! The same simplification that might look good on an ultrawide 4k monitor might not be appropriate for a smartphone screen. It would make sense to simplify this polyline in respect to pixels or some type of display unit, rather than use the real data. 

This is the purpose of the `xTransformer` and `yTransformer` optional functions. They alter the x and y values respectively for the purposes of the simplification algorithms, but do not alter what is actually returned. For our example above, we want to convert all the x and y coordinates to pixels or some display equivalent, simplify that polyline, but still return the correct temperature on the X axis and the time in milliseconds on the Y axis. Take a look at the demo for how this might work in a web context.

## What is it
SimplifyK implements two polyline simplification algorithms. Radial Distance (RD) and Ramer-Douglas-Peucker (RDP). Both algorithms are designed to take points in a polyline and reduce the number of points while retaining the same shape. This is used frequently for charting large datasets where many points would lead to performance issues and visual noise.

SimplifyK like simplify-js applies Radial Distance first which is an algorithm that crawls along a polyline eliminating any points within the radial distance passed in. The result of that is then applied to Ramer-Douglas-Peucker for further simplification. Radial Distance is done first because it is faster than Ramer-Douglas-Peucker and acts as preprocessing.

## Implementation Details
Ramer-Douglas-Peucker is a recursive algorithm. To prevent any stack overflow issues `DeepRecursiveFunction` (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-deep-recursive-function/) from the Kotlin standard library was used. Therefore, this should work on even large non-optimal data sets. 

## Contributing
Contributions are welcome including additional polyline simplification algorithms and performance improvements. Before opening a PR be sure to run tests and static analysis `./gradlew allTest allDetekt`. If you run into issues with the browser tests at least ensure that `./gradlew jvmTest` passes. This project also includes some JVM benchmarks which can be run with `./gradlew bench` if you have performance improvements to propose I would appreciate including benchmarks for each implementation.
