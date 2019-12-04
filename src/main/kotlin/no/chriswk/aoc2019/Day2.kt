package no.chriswk.aoc2019

class Day2 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day = Day2()
            println(day.part1())
            println(day.part2())
        }
    }

    fun part1(): Int {
        val program = "day2.txt".fileToString()
        val instructions = parseProgram(program)
        instructions[1] = 12
        instructions[2] = 2
        return runProgram(instructions)[0]
    }
    fun part2(): Int {
        val program = "day2.txt".fileToString()
        val instructions = parseProgram(program)
        (1..100).forEach { noun ->
            (1..100).forEach { verb ->
                val copy = instructions.copyOf()
                copy[1] = noun
                copy[2] = verb
                if (runProgram(copy)[0] == 19690720) {
                    return 100 * noun + verb
                }
            }
        }
        return -1
    }


    fun parseProgram(program: String): Array<Int> {
        return program.split(",").map { it.toInt() }.toTypedArray()
    }
    fun runProgram(program: Array<Int>): Array<Int> {
        val instructions = program.copyOf()
        var currentPos = 0
        while (instructions[currentPos] != 99) {
            when (instructions[currentPos]) {
                1 -> instructions[instructions[currentPos + 3]] =
                    instructions[instructions[currentPos + 1]] + instructions[instructions[currentPos + 2]]
                2 -> instructions[instructions[currentPos + 3]] =
                    instructions[instructions[currentPos + 1]] * instructions[instructions[currentPos + 2]]
            }
            currentPos += 4
        }
        return instructions
    }

}