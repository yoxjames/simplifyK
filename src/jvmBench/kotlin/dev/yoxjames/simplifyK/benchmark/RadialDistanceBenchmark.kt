package dev.yoxjames.simplifyK.benchmark

import dev.yoxjames.simplifyk.simplifications.radialDistance
import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.runner.Defaults
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = Defaults.WARMUP_ITERATIONS, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
open class RadialDistanceBenchmark {
    @Benchmark
    fun radialDistancePoint2D(): List<Point2D> {
        return points.radialDistance(
            tolerance = 0.1,
            xExtractor = { it.x },
            yExtractor = { it.y }
        )
    }
}
