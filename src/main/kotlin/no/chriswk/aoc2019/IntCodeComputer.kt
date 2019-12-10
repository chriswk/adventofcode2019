package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking

class IntCodeComputer(private val program: IntArray, val input: Channel<Int>) {
    val output: Channel<Int> = Channel(Channel.UNLIMITED)
    companion object {
        operator fun invoke(program: String, input : MutableList<Int> = mutableListOf()) = runBlocking {
            IntCodeComputer(parseInstructions(program), input.toChannel())
        }
        operator fun invoke(program: IntArray, singleInput: Int) = runBlocking {
            IntCodeComputer(program, mutableListOf(singleInput).toChannel())
        }

        operator fun invoke(program: IntArray, input: MutableList<Int> = mutableListOf()) = runBlocking {
            IntCodeComputer(program, input.toChannel())
        }
    }

    fun run(): List<Int> = runBlocking {
        runSuspending()
        output.toList()
    }

    suspend fun runSuspending() {
        var instructionPointer = 0

        do {
            val nextOp = IntCodeInstruction(instructionPointer, program)
            instructionPointer += nextOp.execute(instructionPointer, program, input, output)
        } while (nextOp !is IntCodeInstruction.Halt)

        output.close()
    }
}

