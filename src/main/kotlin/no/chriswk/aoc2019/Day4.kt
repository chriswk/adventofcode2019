package no.chriswk.aoc2019

class Day4 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day4 = Day4()
            report { day4.part1() }
            report { day4.part2() }
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

    private fun isStrictlyIncreasing(digits: List<Int>): Boolean {
        return digits.sorted() == digits
    }

    private fun doubles(digits: List<Int>, req: (Int) -> Boolean): Boolean {
        return digits.groupBy { it }.entries.any { req(it.value.size) }
    }

    private fun isStrictlyIncreasingTake(digits: List<Int>): Boolean {
        //We know that there's exactly 6 digits
        val (a, b, c, d, e, f) = digits
        return a <= b && b <= c && c <= d && d <= e && e <= f
    }

    operator fun List<Int>.component6() = this[5]

    fun isValid(candidate: Int): Boolean {
        val digs = digits(candidate)
        return isStrictlyIncreasingTake(digs) && doubles(digs) { it > 1 }
    }

    fun isValidByPart2(candidate: Int): Boolean {
        val digs = digits(candidate)
        return isStrictlyIncreasingTake(digs) && doubles(digs) { it == 2 }
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