package no.chriswk.aoc2019

class Day5 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day5 = Day5()
            report { day5.part1() }
            report { day5.part2() }
        }
    }

    fun part1(): Int {
        val input = "day5.txt".fileToString()
        val computer = IntCodeComputer.prepare(input, listOf(1))
        val output = computer.run()
        return output.lastOrNull() ?: Int.MIN_VALUE
    }

    fun part2(): Int {
        val input = "day5.txt".fileToString()
        val computer = IntCodeComputer.prepare(input, listOf(5))
        val output = computer.run()
        return output.lastOrNull() ?: Int.MIN_VALUE
    }
}