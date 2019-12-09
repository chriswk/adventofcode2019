package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import java.awt.Adjustable

class IntCodeComputer(val program: IntArray, val input: Channel<Int>) {
    val output: Channel<Int> = Channel(Channel.UNLIMITED)

    companion object {
        operator fun invoke(program: IntArray, input: Int) = runBlocking {
            IntCodeComputer(program, mutableListOf(input).toChannel())
        }

        operator fun invoke(program: IntArray, inputs: MutableList<Int> = mutableListOf()) = runBlocking {
            IntCodeComputer(program, inputs.toChannel())
        }

        fun prepare(instructions: String, inputs: List<Int>): IntCodeComputer {
            return IntCodeComputer(parseInstructions(instructions), inputs = inputs.toMutableList())
        }
    }

    suspend fun runSuspending(startIdx: Int = 0) {
        var ip = startIdx
        var relative = 0
        val memory = program.copyOf()
        do {
            val op = IntCodeOp.fromInt(memory[ip])
            val (dP, dRel) = op.execute(ip, relative, memory, input, output)
            ip += dP
            relative += dRel
        } while (op.action != Action.QUIT)
        println("Finished runSuspending")
    }

    fun run(): List<Int> = runBlocking {
        runSuspending()
        val output = output.toList()
        println("Saw outputs")
        output
    }
}

data class IntCodeOp(val action: Action, val param1Mode: Mode, val param2Mode: Mode, val param3Mode: Mode) {
    companion object {
        fun fromInt(instruction: Int): IntCodeOp {
            val action = Action.fromInt(instruction % 100)
            val param1Mode = Mode.valueOf(instruction % 1000 / 100)
            val param2Mode = Mode.valueOf(instruction % 10000 / 1000)
            val param3Mode = Mode.valueOf(instruction % 100000 / 10000)
            return IntCodeOp(
                action = action,
                param1Mode = param1Mode,
                param2Mode = param2Mode,
                param3Mode = param3Mode
            )
        }
    }

    fun get(memory: IntArray, mode: Mode, address: Int, relative: Int = 0): Int {
        return when (mode) {
            Mode.IMMEDIATE -> memory[address]
            Mode.POSITION -> memory[memory[address]]
            Mode.RELATIVE -> memory[address+relative]
        }
    }

    fun getTwo(memory: IntArray, address1: Int, address2: Int, relative: Int = 0): Pair<Int, Int> {
        val left = when (param1Mode) {
            Mode.IMMEDIATE -> memory[address1]
            Mode.POSITION -> memory[memory[address1]]
            Mode.RELATIVE -> memory[address1 + relative]
        }
        val right = when (param2Mode) {
            Mode.IMMEDIATE -> memory[address2]
            Mode.POSITION -> memory[memory[address2]]
            Mode.RELATIVE -> memory[address2+relative]
        }
        return left to right
    }

    fun assign(memory: IntArray, mode: Mode, address: Int, newVal: Int, relative: Int = 0) {
        when (mode) {
            Mode.IMMEDIATE -> memory[address] = newVal
            Mode.POSITION -> memory[memory[address]] = newVal
            Mode.RELATIVE -> memory[address+relative]
        }
    }

    suspend fun execute(ip: Int, relative: Int, memory: IntArray, input: ReceiveChannel<Int>, output: SendChannel<Int>): Pair<Int, Int> {
        return when (action) {
            Action.ADD -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                assign(memory, param3Mode, ip + 3, left + right)
                return Action.ADD.instructionSize to 0
            }
            Action.MUL -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                assign(memory, param3Mode, ip + 3, left * right)
                Action.MUL.instructionSize to 0
            }
            Action.SAVE -> {
                assign(memory, param1Mode, ip + 1, newVal = input.receive())
                Action.SAVE.instructionSize to 0
            }
            Action.OUT -> {
                output.send(get(memory, param1Mode, ip + 1))
                Action.OUT.instructionSize to 0
            }
            Action.JEQ -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                if (left != 0) {
                    right to 0
                } else {
                    Action.JEQ.instructionSize to 0
                }
            }
            Action.JNEQ -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                if (left == 0) {
                    right to 0
                } else {
                    Action.JNEQ.instructionSize to 0
                }
            }
            Action.LT -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                val newVal = if (left < right) {
                    1
                } else {
                    0
                }
                assign(memory, param3Mode, ip + 3, newVal)
                Action.LT.instructionSize to 0
            }
            Action.EQ -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                val newVal = if (left == right) {
                    1
                } else {
                    0
                }
                assign(memory, param3Mode, ip + 3, newVal)
                Action.EQ.instructionSize to 0
            }
            Action.ADJUST -> {
                val newRelative = get(memory, mode = param1Mode, address = ip+1, relative = relative)
                Action.ADJUST.instructionSize to newRelative
            }
            Action.QUIT -> {
                Action.QUIT.instructionSize to 0
            }
        }
    }
}

enum class Mode {
    POSITION, IMMEDIATE, RELATIVE;

    companion object {
        fun valueOf(value: Int): Mode {
            return when (value) {
                1 -> IMMEDIATE
                2 -> RELATIVE
                else -> POSITION
            }
        }
    }
}

enum class Action(val instructionSize: Int) {
    MUL(4), ADD(4), SAVE(2), OUT(2), JEQ(3), JNEQ(3), LT(4), EQ(4), QUIT(0), ADJUST(2);

    companion object {
        fun fromInt(instruction: Int): Action {
            return when (instruction) {
                1 -> ADD
                2 -> MUL
                3 -> SAVE
                4 -> OUT
                5 -> JEQ
                6 -> JNEQ
                7 -> LT
                8 -> EQ
                9 -> ADJUST
                99 -> QUIT
                else -> error("Unknown opcode ${instruction}")
            }
        }
    }
}