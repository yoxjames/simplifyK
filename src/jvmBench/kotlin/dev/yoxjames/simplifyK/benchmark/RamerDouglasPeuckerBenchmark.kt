package dev.yoxjames.simplifyK.benchmark

import dev.yoxjames.simplifyk.simplifications.ramerDouglasPeucker
import kotlinx.benchmark.Scope
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.runner.Defaults.WARMUP_ITERATIONS
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = WARMUP_ITERATIONS, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
open class RamerDouglasPeuckerBenchmark {
    companion object {
        private const val SIZE = 100
        private const val Y_SCALE_FACTOR = 0.01
    }
    private lateinit var realPoints: List<Point2D>
    private lateinit var zipZagPoints: List<Point2D>

    private fun generateZigZagLine(size: Int, yScaleFactor: Double): List<Point2D> {
        return (0..size).map {
            Point2D(x = it.toDouble(), y = (if (it % 2 == 0) -it else it).toDouble() * yScaleFactor)
        }
    }

    @Setup
    fun setup() {
        zipZagPoints = generateZigZagLine(SIZE, Y_SCALE_FACTOR)
        realPoints = points
    }

    @Benchmark
    fun rdpPoint2DZigZag(): List<Point2D> {
        return zipZagPoints.ramerDouglasPeucker(
            epsilon = 0.1,
            xExtractor = { it.x },
            yExtractor = { it.y }
        )
    }

    @Benchmark
    fun rdpPoint2DReal(): List<Point2D> {
        return realPoints.ramerDouglasPeucker(
            epsilon = 0.1,
            xExtractor = { it.x },
            yExtractor = { it.y }
        )
    }
}
