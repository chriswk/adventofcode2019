package no.chriswk.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class IntCodeComputer(val program: MutableMap<Long, Long>,
                      val input: Channel<Long> = Channel(Channel.UNLIMITED), val output: Channel<Long> = Channel(Channel.UNLIMITED)) {
    companion object {
        operator fun invoke(program: IntArray) = runBlocking {
            IntCodeComputer(program.toMutableMap())
        }
        operator fun invoke(program: String, input : MutableList<Long> = mutableListOf()) = runBlocking {
            IntCodeComputer(parseInstructions(program).toMutableMap(), input.toChannel())
        }

        operator fun invoke(program: IntArray, input: MutableList<Long> = mutableListOf()) = runBlocking {
            IntCodeComputer(program.toMutableMap(), input.toChannel())
        }
    }
    fun launch(): Job = GlobalScope.launch {
        try {
            runSuspending()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    fun run(): List<Long> = runBlocking {
        runSuspending()
        output.toList()
    }

    suspend fun runSuspending() {
        var instructionPointer = 0L
        var relative = 0L
        do {
            val nextOp = IntCodeInstruction(instructionPointer, program)
            val (newIp, newRel) = nextOp.execute(instructionPointer, program, input, output, relative)
            instructionPointer = newIp
            relative = newRel
        } while (nextOp !is IntCodeInstruction.Halt)

        output.close()
    }
}

