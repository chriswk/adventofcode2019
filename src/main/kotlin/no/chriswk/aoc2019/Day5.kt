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

    fun part1(): Long {
        val program = parseInstructions("day5.txt".fileToString())
        val computer = IntCodeComputer(program.toMutableMap(), input = mutableListOf(1L).toChannel())
        val output = computer.run()
        return output.lastOrNull() ?: Long.MIN_VALUE
    }

    fun part2(): Long {
        val program = parseInstructions("day5.txt".fileToString())
        val computer = IntCodeComputer(program.toMutableMap(), input = mutableListOf(5L).toChannel())
        val output = computer.run()
        return output.lastOrNull() ?: Long.MIN_VALUE
    }
}