# SimplifyK

This is a small Kotlin Multiplatform library for simplification of polylines. It is essentially a port of [simplify-js](https://mourner.github.io/simplify-js/) in common Kotlin.

## Quick Start
To

## What is it
SimplifyK implements two polyline simplification algorithms. Radial Distance (RD) and Ramer-Douglas-Peucker (RDP). Both algoritms are designed to take points in a polyline and reduce the number of points while retaining the same general shape. This is used frequently for charting large datasets where many points would lead to performance issues and visual noise.

Simplify-js applies Radial Distance first which is an algorithm that crawls along a polyline eliminating any points within the radial distance passed in. The result of that is then applied to Ramer-Douglas-Peucker for further simplification. Radial Distance is done first because it is faster than Ramer-Douglas-Peucker.

## Implementation Details
Ramer-Douglas-Peucker is a recursive algorithm. To prevent any stack overflow issues `DeepRecursiveFunction` (https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-deep-recursive-function/) from the Kotlin standard library was used. Therefore, this should work on even large non-optimal data sets. 