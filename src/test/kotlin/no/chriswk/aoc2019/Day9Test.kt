package no.chriswk.aoc2019

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day9Test {

    @Test
    fun part1() {
        val day9 = Day9()
        assertThat(day9.part1()).isEqualTo(3063082071)
    }

    @Test
    fun part2() {
        val day9 = Day9()
        assertThat(day9.part2()).isEqualTo(81348)
    }

    @Test
    fun `should produce a copy of itself as output - quine`() {
        runBlocking {
            val program = parseBigInstructions("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99")
            val computer = IntCodeComputer(program.toMutableMap(), input = mutableListOf<Long>().toChannel())
            val output = computer.run()
            assertThat(output.toLongArray()).isEqualTo(program)
        }
    }

    @Test
    fun `should output large number`() {
        runBlocking {
            val program = parseBigInstructions("104,1125899906842624,99").toMutableMap()
            val computer = IntCodeComputer(program)
            val output = computer.run()
            assertThat(output).containsExactly(1125899906842624)
        }
    }
}