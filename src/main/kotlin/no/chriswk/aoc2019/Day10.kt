package no.chriswk.aoc2019

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

class Day10 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day10 = Day10()
            report { day10.part1() }
            report { day10.part2() }
        }
    }

    fun parseInput(input: List<String>): List<Point> {
        return input.withIndex()
            .flatMap { (y, row) -> row.withIndex().filter { it.value == '#' }.map { Point(it.index, y) } }
    }

    fun part1(): Int {
        val input = "day10.txt".fileToLines()
        val asteroids = parseInput(input)
        return visible(asteroids).max() ?: -1
    }

    fun part2(): Int {
        val input = "day10.txt".fileToLines()
        val asteroids = parseInput(input)
        val station = findStation(asteroids)!!
        val other = (asteroids - station)
        val twohundrethTarget = shoot(station, other).drop(199).first()
        return twohundrethTarget.part2()
    }

    fun shoot(station: Point, other: List<Point>): Sequence<Point> {
        val remaining = other.toMutableList()
        var angle = -PI / 2
        var firstTarget = true
        return generateSequence {
            val asteroidsByAngle = remaining.groupBy { atan2(it.dy(station).toDouble(), it.dx(station).toDouble()) }
            val nextAngleTargets = asteroidsByAngle
                .filter { if (firstTarget) it.key >= angle else it.key > angle }
                .minBy { it.key }
                ?: asteroidsByAngle.minBy { it.key }!!
            angle = nextAngleTargets.key
            firstTarget = false
            val target =
                nextAngleTargets.value.minBy { hypot(it.dx(station).toDouble(), it.dy(station).toDouble()) }!!
            remaining.remove(target)
            target
        }
    }

    fun findStation(points: List<Point>): Point? {
        return points.maxBy { asteroid ->
            points.filter { it != asteroid }
                .map { atan2(it.dy(asteroid).toDouble(), it.dx(asteroid).toDouble()) }
                .distinct()
                .count()
        }
    }

    fun visible(points: List<Point>): List<Int> {
        return points.map { asteroid ->
            points.filter { it != asteroid }
                .map { atan2(it.dy(asteroid).toDouble(), it.dx(asteroid).toDouble()) }
                .distinct()
                .count()
        }
    }
}
fun Point.part2() = this.x * 100 + this.y


