package no.chriswk.aoc2019

import kotlinx.coroutines.runBlocking

class Day15 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day15 = Day15()
            val program = parseBigInstructions("day15.txt".fileToString()).toMutableMap()
            report { day15.part1(program) }
            report { day15.part2(program) }
        }
    }

    fun part1(program: MutableMap<Long, Long>): Int {
        val (gridLimits, points) = runBlocking {
            buildGrid(
                IntCodeComputer(program),
                Point.ORIGIN
            )
        }
        val oxygenAt = points.entries.first { it.value == 2L }.key
        val paths = pathsFromTo(src = oxygenAt, dest = Point.ORIGIN, points = points)
        return paths.map { it.size }.min() ?: -1
    }
    fun part2(program: MutableMap<Long, Long>): Int {
        val (gridLimits, points) = runBlocking {
            buildGrid(IntCodeComputer(program), Point.ORIGIN)
        }
        val oxygenAt = points.entries.first { it.value == 2L }.key
        val distances = mutableMapOf<Point, Int>()
        distancesFrom(oxygenAt, points.filterNot { it.value == 0L }, distances)
        return distances.values.max()!!
    }
    fun distancesFrom(current: Point, grid: Map<Point, Long>, distances: MutableMap<Point, Int>, distance: Int = 0) {
        if (!grid.containsKey(current)) { return }
        val curMin = distances[current] ?: (distance + 1)
        if (distance < curMin) {
            distances[current] = distance
            current.cardinalNeighbours().forEach { (n, d) ->
                distancesFrom(n, grid, distances, distance + 1)
            }
        }
    }


    fun pathsFromTo(
        src: Point,
        dest: Point,
        visited: List<Point> = listOf(),
        points: Map<Point, Long>
    ): List<List<Point>> {
        return if (src == dest) {
            return listOf(visited)
        } else {
            src.cardinalNeighbours()
                .filterNot { visited.contains(it.first) }
                .filter { points[it.first] != 0L }
                .flatMap { pathsFromTo(it.first, dest, visited + it.first, points) }
        }
    }

    suspend fun buildGrid(vm: IntCodeComputer, start: Point): Pair<Grid, Map<Point, Long>> {
        vm.launch()
        val positions = mutableMapOf(start to 1L)
        track(start, positions, vm)
        return grid(positions = positions.keys) to positions
    }

    suspend fun track(point: Point, seen: MutableMap<Point, Long>, vm: IntCodeComputer) {
        point.cardinalNeighbours().filterNot { seen.containsKey(it.first) }.forEach { (p, dir) ->
            val block = move(vm, dir.cmd())
            seen[p] = block
            when (block) {
                0L -> {
                }
                else -> {
                    track(p, seen, vm)
                    move(vm, dir.back())
                }
            }
        }
    }

    suspend fun move(vm: IntCodeComputer, cmd: Long): Long {
        vm.input.send(cmd)
        return vm.output.receive()
    }

    data class Grid(val topLeft: Point, val bottomRight: Point) {
        fun print(positions: Map<Point, Long>): String {
            return topLeft.y.downTo(bottomRight.y).joinToString(separator = "\n") { y ->
                (topLeft.x..bottomRight.x).joinToString(separator = "") { x ->
                    if (y == 0 && x == 0) {
                        "R"
                    } else {
                        when (positions.getOrDefault(Point(x, y), 0)) {
                            0L -> "#"
                            1L -> " "
                            2L -> "O"
                            else -> "X"
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