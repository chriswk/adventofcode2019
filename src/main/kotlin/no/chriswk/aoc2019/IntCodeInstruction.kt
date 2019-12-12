package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.math.pow

sealed class IntCodeInstruction(internal val nextInstructionOffset: Long) {
    companion object {
        operator fun invoke(pointer: Long, program: MutableMap<Long, Long>): IntCodeInstruction {
            return when (val operation = program.getOrDefault(pointer, 0) % 100) {
                1L -> Add
                2L -> Multiply
                3L -> Input
                4L -> Output
                5L -> JumpIfTrue
                6L -> JumpIfFalse
                7L -> LessThan
                8L -> Equals
                9L -> Relative
                99L -> Halt
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
        }
    }

    fun readParam(program: MutableMap<Long, Long>, instructionPointer: Long, paramNo: Int, relative: Long = 0): Long =
        program.getReadRef(program.getOrDefault(instructionPointer, 0L).toParameterMode(paramNo), instructionPointer + paramNo, relative)

    fun writeParam(program: MutableMap<Long, Long>, instructionPointer: Long, paramNo: Int, relative: Long = 0): Long =
        program.getWriteRef(program.getOrDefault(instructionPointer, 0L).toParameterMode(paramNo), instructionPointer + paramNo, relative)

    fun Long.toParameterMode(pos: Int): Int =
        (this / (10.0.pow(pos + 1).toInt()) % 10).toInt()

    fun MutableMap<Long, Long>.getReadRef(mode: Int, at: Long, rel: Long): Long =
        when (mode) {
            0 -> getOrDefault(getOrDefault(at, 0L), 0L)
            1 -> getOrDefault(at, 0L)
            2 -> getOrDefault(getOrDefault(at, 0) + rel, 0)
            else -> throw IllegalArgumentException("Unknown read mode: $mode")
        }

    fun MutableMap<Long, Long>.getWriteRef(mode: Int, at: Long, rel: Long): Long =
        when (mode) {
            0 -> getOrDefault(at, 0L)
            2 -> getOrDefault(at, 0L) + rel
            else -> throw IllegalArgumentException("Unknown write mode: $mode")
        }
    abstract suspend fun execute(
        pointer: Long,
        program: MutableMap<Long, Long>,
        input: ReceiveChannel<Long>,
        output: Channel<Long>,
        relative: Long
    ): Pair<Long, Long>
    object Add : IntCodeInstruction(4) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            val writeTo = writeParam(program, pointer, 3, relative)
            program[writeTo] = readParam(program, pointer, 1, relative) + readParam(program, pointer, 2, relative)
            return pointer + nextInstructionOffset to relative
        }
    }


    object Multiply : IntCodeInstruction(4) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            val writeTo = writeParam(program, pointer, 3, relative)
            program[writeTo] = readParam(program, pointer, 1, relative) * readParam(program, pointer, 2, relative)
            return pointer + nextInstructionOffset to relative
        }
    }

    object Input : IntCodeInstruction(2) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            val writeTo = writeParam(program, pointer, 1, relative)
            program[writeTo] = input.receive()
            return pointer + nextInstructionOffset to relative
        }
    }

    object Output : IntCodeInstruction(2) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            output.send(readParam(program, pointer, 1, relative))
            return pointer + nextInstructionOffset to relative
        }
    }

    object JumpIfTrue : IntCodeInstruction(3) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            val newOffset = readParam(program, pointer, 1, relative)
            return if (newOffset != 0L) {
                readParam(program, pointer, 2, relative) to relative
            } else {
                pointer + nextInstructionOffset to relative
            }
        }

    }

    object JumpIfFalse : IntCodeInstruction(3) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            return if (readParam(program, pointer,1, relative) == 0L) {
                readParam(program, pointer, 2, relative) to relative
            } else {
                pointer + nextInstructionOffset to relative
            }
        }
    }

    object LessThan : IntCodeInstruction(4) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            val writeParam = writeParam(program, pointer, 3, relative)
            program[writeParam] = if (readParam(program, pointer,1, relative) < readParam(program, pointer, 2, relative)) {
                1L
            } else {
                0L
            }
            return pointer + nextInstructionOffset to relative
        }
    }

    object Equals : IntCodeInstruction(4) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            val writeParam = writeParam(program, pointer, 3, relative)
            program[writeParam] = if (readParam(program, pointer, 1, relative) == readParam(program, pointer, 2, relative)) {
                1L
            } else {
                0L
            }
            return pointer + nextInstructionOffset to relative
        }

    }

    object Relative : IntCodeInstruction(2) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            return pointer + nextInstructionOffset to relative + readParam(program, pointer, 1, relative)
        }
    }
    object Halt : IntCodeInstruction(1) {
        override suspend fun execute(
            pointer: Long,
            program: MutableMap<Long, Long>,
            input: ReceiveChannel<Long>,
            output: Channel<Long>,
            relative: Long
        ): Pair<Long, Long> {
            return pointer + 0L to 0L
        }
    }
}