package no.chriswk.aoc2019

import java.lang.IllegalStateException

class Day2 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day = Day2()
            report { day.part1() }
            report { day.part2() }
        }
    }

    fun part1(): Long {
        val program = "day2.txt".fileToString()
        val instructions = parseInstructions(program)
        instructions[1] = 12
        instructions[2] = 2
        val computer = IntCodeComputer(instructions)
        computer.run()
        return computer.program[0] ?: throw IllegalStateException("No memory in position 0")
    }

    fun part2(): Int {
        val program = "day2.txt".fileToString()
        val instructions = parseInstructions(program)
        (1..100).forEach { noun ->
            (1..100).forEach { verb ->
                val copy = instructions.copyOf()
                copy[1] = noun
                copy[2] = verb
                val p = IntCodeComputer(copy)
                p.run()
                if (p.program[0] == 19690720L) {
                    return 100 * noun + verb
                }
            }
        }
        return -1
    }
}