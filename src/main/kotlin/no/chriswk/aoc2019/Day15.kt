package no.chriswk.aoc2019

import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class Day15 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day15 = Day15()
            report { day15.part1() }
        }
    }

    fun part1(): Long {
        val (gridLimits, points) = runBlocking {
            buildGrid(
                IntCodeComputer(parseBigInstructions("day15.txt".fileToString()).toMutableMap()),
                Point.ORIGIN
            )
        }
        println(gridLimits.print(points))
        //val oxygenAt = points.entries.first { it.value == 2L }
        //val startAt = Point(-gridLimits.topLeft.x, -gridLimits.bottomRight.x)
        return -1L
    }

    suspend fun buildGrid(vm: IntCodeComputer, start: Point): Pair<Grid, Map<Point, Long>> {
        vm.launch()
        val positions = mutableMapOf(start to 1L)
        track(start, positions, vm)
        val gridLimits = grid(positions = positions.keys)
        return gridLimits to positions
    }

    suspend fun track(point: Point, seen: MutableMap<Point, Long>, vm: IntCodeComputer) {
        point.cardinalNeighbours().filterNot { seen.containsKey(it.first) }.forEach { (p, dir) ->
            val block = move(vm, dir.cmd())
            seen[p] = block
            when (block) {
                0L -> {
                }
                1L -> {
                    track(p, seen, vm)
                    move(vm, dir.back())
                }
                2L -> {
                    track(p, seen, vm)
                    move(vm, dir.back())
                    println("Found the oxygen at $p")
                }
                else -> throw IllegalStateException("Got unexpected output from vm")
            }
        }
    }

    suspend fun move(vm: IntCodeComputer, cmd: Long): Long {
        vm.input.send(cmd)
        val output = vm.output.receive()
        println(output)
        return output
    }
    fun Long.toDirection(): String {
        return when(this) {
            1L -> "N"
            2L -> "S"
            3L -> "W"
            4L -> "E"
            else -> throw IllegalArgumentException("No such direction")
        }
    }

    data class Grid(val topLeft: Point, val bottomRight: Point) {
        fun print(positions: Map<Point, Long>): String {
            return topLeft.y.downTo(bottomRight.y).joinToString(separator = "\n") { y ->
                (topLeft.x..bottomRight.x).joinToString(separator = "") { x ->
                    if (y == 0 && x == 0) { "R" } else {
                        when (positions.getOrDefault(Point(x, y), 0)) {
                            0L -> "#"
                            1L -> " "
                            2L -> "O"
                            else -> ""
                        }
                    }
                }
            }
        }
    }

    fun grid(positions: MutableSet<Point>): Grid {
        val upperLeftX = positions.minBy { it.x }!!.x
        val upperLeftY = positions.maxBy { it.y }!!.y
        val lowerRightX = positions.maxBy { it.x }!!.x
        val lowerRightY = positions.minBy { it.y }!!.y
        return Grid(Point(upperLeftX, upperLeftY), Point(lowerRightX, lowerRightY))
    }


}