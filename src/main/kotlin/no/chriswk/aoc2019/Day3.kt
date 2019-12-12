package no.chriswk.aoc2019

import kotlin.math.abs

class Day3 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = Day3()

            report { d.part1() }
            report { d.part2() }
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
    constructor(code: String) : this(Direction.valueOf(code.first().toUpperCase().toString()), code.drop(1).toInt())
}

enum class Direction {
    U, D, L, R
}

typealias Route = List<Point>