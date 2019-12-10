package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day9Test {

    @Test
    fun `should produce a copy of itself as output - quine`() {
        runBlocking {
            val program = parseInstructions("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99")
            val output = Channel<Long>(Channel.UNLIMITED)
            val computer = IntCodeComputerMemory(program.toMutableMap(), output = output)
            computer.runProgram()
            assertThat(output.toList().map { it.toInt() }.toIntArray()).isEqualTo(program)
        }
    }

    @Test
    fun `should output large number`() {
        runBlocking {
            val program = parseBigInstructions("104,1125899906842624,99").toMutableMap()
            val output = Channel<Long>(Channel.UNLIMITED)
            val computer = IntCodeComputerMemory(program, output = output)
            computer.runProgram()
            assertThat(output.toList()).containsExactly(1125899906842624)
        }
    }
}