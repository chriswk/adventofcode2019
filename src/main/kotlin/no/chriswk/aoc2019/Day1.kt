package no.chriswk.aoc2019

class Day1 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d1 = Day1()
            println(d1.part1())
            println(d1.part2())
        }

    }

    fun part1(): Int {
        return "day1.txt".fileToLines()
            .map { it.toInt() }
            .map { findFuel(it) }
            .sum()
    }
    fun part2(): Int {
        return "day1.txt".fileToLines()
            .map { it.toInt() }
            .map { findFuel2(it) }
            .sum()
    }

    fun findFuel2(weight: Int): Int {
        return generateSequence(findFuel(weight)) { w ->
            findFuel(w)
        }.takeWhile { it > 0 }.sum()
    }
    fun findFuel(weight: Int): Int {
        return Math.floorDiv(weight, 3) - 2
    }
}

