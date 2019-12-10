package no.chriswk.aoc2019

import kotlinx.coroutines.runBlocking

class Day9 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day9 = Day9()
            report { day9.part1() }
            report { day9.part2() }
        }
    }

    fun part1(): Long = runBlocking {
        val program = parseInstructions("day9.txt".fileToString()).toMutableMap()
        IntCodeComputerMemory(program).run {
            input.send(1)
            runProgram()
            output.receive()
        }
    }

    fun part2(): Long = runBlocking {
        val program = parseInstructions("day9.txt".fileToString()).toMutableMap()
        IntCodeComputerMemory(program).run {
            input.send(2)
            runProgram()
            output.receive()
        }
    }
}