package no.chriswk.aoc2019

class Day7(val program: IntArray) {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day7 = Day7(parseInstructions("day7.txt".fileToString()))
            report { day7.part1() }
        }
    }

    fun part1(): Int {
        return listOf(0,1,2,3,4).permutations().map { run(settings = it) }.max() ?: Int.MIN_VALUE
    }
    fun run(settings: List<Int>): Int {
        return (0..4).fold(0) { prev, id ->
            val (_, outputs) = IntCodeComputer(program = program.copyOf()).run(inputs = mutableListOf(settings[id], prev))
            outputs.lastOrNull() ?: Int.MIN_VALUE
        }
    }

}