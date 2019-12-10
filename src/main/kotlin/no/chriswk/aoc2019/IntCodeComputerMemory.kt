package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlin.math.pow

class IntCodeComputerMemory(
    private val program: MutableMap<Long, Long>,
    val input: Channel<Long> = Channel(Channel.UNLIMITED),
    val output: Channel<Long> = Channel(Channel.CONFLATED)
) {
    private var halted = false
    private var ip = 0L
    private var rel = 0L

    suspend fun runProgram() {
        while (!halted) {
            step()
        }
        output.close()
    }

    private suspend fun step() {
        when (val opId = (program.getValue(ip) % 100L).toInt()) {
            1 -> {
                program[writeParam(3)] = readParam(1) + readParam(2)
                ip += 4
            }
            2 -> {
                program[writeParam(3)] = readParam(1) * readParam(2)
                ip += 4
            }
            3 -> {
                program[writeParam(1)] = input.receive()
                ip += 2
            }
            4 -> {
                output.send(readParam(1))
                ip += 2
            }
            5 -> {
                if (readParam(1) != 0L) {
                    ip = readParam(2)
                } else {
                    ip += 3
                }
            }
            6 -> {
                if (readParam(1) == 0L) {
                    ip = readParam(2)
                } else {
                    ip += 3
                }
            }
            7 -> {
                program[writeParam(3)] = if (readParam(1) < readParam(2)) 1L else 0L
                ip += 4
            }
            8 -> {
                program[writeParam(3)] = if (readParam(1) == readParam(2)) 1L else 0L
                ip += 4
            }
            9 -> {
                rel += readParam(1)
                ip += 2
            }
            99 -> {
                halted = true
            }
            else -> throw java.lang.IllegalArgumentException("Unknown operation: $opId")
        }
    }

    private fun readParam(paramNo: Int): Long =
        program.getReadRef(program.getOrDefault(ip, 0L).toParameterMode(paramNo), ip + paramNo)

    private fun writeParam(paramNo: Int): Long =
        program.getWriteRef(program.getOrDefault(ip, 0L).toParameterMode(paramNo), ip + paramNo)

    private fun Long.toParameterMode(pos: Int): Int =
        (this / (10.0.pow(pos + 1).toInt()) % 10).toInt()

    private fun MutableMap<Long, Long>.getReadRef(mode: Int, at: Long): Long =
        when (mode) {
            0 -> getOrDefault(getOrDefault(at, 0L), 0L)
            1 -> getOrDefault(at, 0L)
            2 -> getOrDefault(getOrDefault(at, 0) + rel, 0)
            else -> throw IllegalArgumentException("Unknown read mode: $mode")
        }

    private fun MutableMap<Long, Long>.getWriteRef(mode: Int, at: Long): Long =
        when (mode) {
            0 -> getOrDefault(at, 0L)
            2 -> getOrDefault(at, 0L) + rel
            else -> throw IllegalArgumentException("Unknown write mode: $mode")
        }
}