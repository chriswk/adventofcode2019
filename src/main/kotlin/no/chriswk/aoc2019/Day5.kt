package no.chriswk.aoc2019

class Day5 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day5 = Day5()
            day5.part1()
            day5.part2()
        }
    }

    fun part1() {
        val input = "day5.txt".fileToString()
        val computer = IntCodeComputer.parse(input)
        val (memory, output) = computer.run(inputs = listOf(1))
        println(output)
    }

    fun part2() {
        val input = "day5.txt".fileToString()
        val computer = IntCodeComputer.parse(input)
        val (memory, output) = computer.run(inputs = listOf(5))
        println(output)
    }
}