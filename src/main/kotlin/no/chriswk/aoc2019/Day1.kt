package no.chriswk.aoc2019

import java.nio.charset.Charset

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
        return "day1.txt".toInputStream()
            .readBytes()
            .toString(Charset.forName("UTF-8"))
            .split("\n")
            .map { it.toInt() }
            .map { findFuel(it) }
            .sum()
    }
    fun part2(): Int {
        return "day1.txt".toInputStream()
            .readBytes()
            .toString(Charset.forName("UTF-8"))
            .split("\n")
            .map { it.toInt() }
            .map { findFuel2(it) }
            .sum()
    }

    fun findFuel2(weight: Int): Int {
        var total = 0
        var w = weight
        do {
            w = findFuel(w)
            if (w > 0) {
                total += w
            }
        } while (w > 0)
        return total
    }
    fun findFuel(weight: Int): Int {
        return Math.floorDiv(weight, 3) - 2
    }
}

