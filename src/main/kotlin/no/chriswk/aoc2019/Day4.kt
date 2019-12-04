package no.chriswk.aoc2019

class Day4 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day4 = Day4()
            println(day4.part1())
            println(day4.part2())
        }
    }

    fun part1(): Int {
        val (begin, end) = "day4.txt".fileToString().split("-").map { it.toInt() }
        return begin.until(end).count { isValid(it) }
    }
    fun part2(): Int {
        val (begin, end) = "day4.txt".fileToString().split("-").map { it.toInt() }
        return begin.until(end).count { isValidByPart2(it) }
    }

    fun isValid(candidate: Int): Boolean {
        val digs = digits(candidate)
        val isStrictlyIncreasing = digs.sorted() == digs
        val hasDouble = digs.groupBy { it }.entries.any { it.value.size > 1 }
        return isStrictlyIncreasing && hasDouble
    }

    fun isValidByPart2(candidate: Int): Boolean {
        val digs = digits(candidate)
        val isStrictlyIncreasing = digs.sorted() == digs
        val hasDouble = digs.groupBy { it }.entries.any { it.value.size == 2 }
        return isStrictlyIncreasing && hasDouble
    }

    fun digits(number: Int): List<Int> {
        val digits = mutableListOf<Int>()
        var current = number
        while (current > 0) {
            digits.add(current % 10)
            current /= 10
        }
        return digits.reversed()
    }
}