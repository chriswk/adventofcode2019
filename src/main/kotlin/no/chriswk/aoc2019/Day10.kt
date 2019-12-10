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

    fun parseInput(input: List<String>): List<Asteroid> {
        return input.withIndex()
            .flatMap { (y, row) -> row.withIndex().filter { it.value == '#' }.map { Asteroid(it.index, y) } }
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
        return twohundrethTarget.part2
    }


    fun shoot(station: Asteroid, other: List<Asteroid>): Sequence<Asteroid> {
        var remaining = other.toMutableList()
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

    fun findStation(asteroids: List<Asteroid>): Asteroid? {
        return asteroids.maxBy { asteroid ->
            asteroids.filter { it != asteroid }
                .map { atan2(it.dy(asteroid).toDouble(), it.dx(asteroid).toDouble()) }
                .distinct()
                .count()
        }
    }

    fun visible(asteroids: List<Asteroid>): List<Int> {
        return asteroids.map { asteroid ->
            asteroids.filter { it != asteroid }
                .map { atan2(it.dy(asteroid).toDouble(), it.dx(asteroid).toDouble()) }
                .distinct()
                .count()
        }
    }

    data class Asteroid(val x: Int, val y: Int) {
        fun dx(other: Asteroid): Int = x - other.x
        fun dy(other: Asteroid): Int = y - other.y
        val part2 = x*100 + y
    }

}

