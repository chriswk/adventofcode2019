package no.chriswk.aoc2019

import kotlin.math.abs

class Day3 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = Day3()

            println(d.part1())
            println(d.part2())
        }
        val centralPort = Point(0, 0)
    }

    fun part1(): Int {
        val (routeA, routeB) = "day3.txt".fileToLines().map {
            centralPort.route(it)
        }
        return manhattanForClosest(routeCollides(routeA, routeB))
    }
    fun part2(): Int {
        val (routeA, routeB) = "day3.txt".toInputStream().lines().map {
            centralPort.route(it)
        }
        return closestToCentralBySteps(routeA, routeB)
    }

    fun routeCollides(routeA: Route, routeB: Route): Set<Point> {
        return routeA.toSet().intersect(routeB.toSet())
    }
    fun closestToCentral(collisions: Set<Point>): Point {
        return collisions.minBy { it.manhattan(centralPort) }!!
    }
    fun manhattanForClosest(collisions: Set<Point>): Int {
        return closestToCentral(collisions).manhattan(centralPort)
    }

    fun closestToCentralBySteps(routeA: Route, routeB: Route): Int {
        return routeCollides(routeA, routeB).map {
            steps(routeA, it) + steps(routeB, it)
        }.min() ?: 0
    }

    fun steps(route: List<Point>, point: Point): Int {
        return 1 + route.indexOfFirst { it == point }
    }
}
data class Instruction(val d: Direction, val c: Int) {
    constructor(code: String): this(Direction.valueOf(code.first().toUpperCase().toString()), code.drop(1).toInt())
}
enum class Direction {
    U, D, L, R
}
data class Point(val x: Int, val y: Int) {
    fun manhattan(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun route(s: String): Route {
        val route = s.split(",").asSequence().map {
            Instruction(it)
        }
        return route.fold(listOf()) { acc, (f, i) ->
            val point = if (acc.isEmpty()) {
                this
            } else {
                acc.last()
            }
            when(f) {
                Direction.U -> acc + point.up(i).toList()
                Direction.R -> acc + point.right(i).toList()
                Direction.L -> acc + point.left(i).toList()
                Direction.D -> acc + point.down(i).toList()
            }
        }
    }
    fun right(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(x = it.x+1)
        }.drop(1).take(count)
    }
    fun left(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(x = it.x-1)
        }.drop(1).take(count)
    }
    fun up(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(y = it.y+1)
        }.drop(1).take(count)
    }
    fun down(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(y = it.y-1)
        }.drop(1).take(count)
    }
}

typealias Route = List<Point>