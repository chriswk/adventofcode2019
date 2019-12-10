package no.chriswk.aoc2019

import kotlin.math.atan2

class Day10 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day10 = Day10()
            report { day10.part1() }
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
    }

}

