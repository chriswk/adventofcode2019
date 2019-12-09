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
        val computer = IntCodeComputer.parse(input)
        val output = computer.run(inputs = listOf(1))
        return output.lastOrNull() ?: Int.MIN_VALUE
    }

    fun part2(): Int {
        val input = "day5.txt".fileToString()
        val computer = IntCodeComputer.parse(input)
        val (memory, output) = computer.run(inputs = listOf(5))
        return output.lastOrNull() ?: Int.MIN_VALUE
    }
}