package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking

class IntCodeComputer(val program: IntArray, val input: Channel<Int>) {
    val output: Channel<Int> = Channel(Channel.UNLIMITED)

    companion object {
        operator fun invoke(program: IntArray, input: Int) = runBlocking {
            IntCodeComputer(program, mutableListOf(input).toChannel())
        }

        operator fun invoke(program: IntArray, inputs: MutableList<Int> = mutableListOf()) = runBlocking {
            IntCodeComputer(program, inputs.toChannel())
        }

        fun parse(instructions: String): IntCodeComputer {
            return IntCodeComputer(program = parseInstructions(instructions))
        }
        fun prepare(instructions: String, inputs: List<Int>): IntCodeComputer {
            return IntCodeComputer(parseInstructions(instructions), inputs = inputs)
        }
    }

    suspend fun runSuspending(startIdx: Int = 0) {
        var ip = startIdx
        val memory = program.copyOf()
        do {
            val op = IntCodeOp.fromInt(memory[ip])
            ip += op.execute(ip, memory, input, output)
        } while (op.action != Action.QUIT)
    }

    fun run(): List<Int> = runBlocking {
        runSuspending()
        output.toList()
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

    fun get(memory: IntArray, mode: Mode, address: Int): Int {
        return when (mode) {
            Mode.IMMEDIATE -> memory[address]
            Mode.POSITION -> memory[memory[address]]
        }
    }

    fun getTwo(memory: IntArray, address1: Int, address2: Int): Pair<Int, Int> {
        val left = when (param1Mode) {
            Mode.IMMEDIATE -> memory[address1]
            Mode.POSITION -> memory[memory[address1]]
        }
        val right = when (param2Mode) {
            Mode.IMMEDIATE -> memory[address2]
            Mode.POSITION -> memory[memory[address2]]
        }
        return left to right
    }

    fun assign(memory: IntArray, mode: Mode, address: Int, newVal: Int) {
        when (mode) {
            Mode.IMMEDIATE -> memory[address] = newVal
            Mode.POSITION -> memory[memory[address]] = newVal
        }
    }

    suspend fun execute(ip: Int, memory: IntArray, input: Channel<Int>, output: Channel<Int>): Int {
        return when (action) {
            Action.ADD -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                assign(memory, param3Mode, ip + 3, left + right)
                4
            }
            Action.MUL -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                assign(memory, param3Mode, ip + 3, left * right)
                4
            }
            Action.SAVE -> {
                assign(memory, param1Mode, ip + 1, newVal = input.receive())
                2
            }
            Action.OUT -> {
                output.send(get(memory, param1Mode, ip + 1))
                2
            }
            Action.JEQ -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                if (left != 0) {
                    right
                } else {
                    3
                }
            }
            Action.JNEQ -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                if (left == 0) {
                    right
                } else {
                    3
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
                4
            }
            Action.EQ -> {
                val (left, right) = getTwo(memory, ip + 1, ip + 2)
                val newVal = if (left == right) {
                    1
                } else {
                    0
                }
                assign(memory, param3Mode, ip + 3, newVal)
                4
            }
            Action.QUIT -> {
                0
            }
        }
    }
}

enum class Mode {
    POSITION, IMMEDIATE;

    companion object {
        fun valueOf(value: Int): Mode {
            return when (value) {
                1 -> IMMEDIATE
                else -> POSITION
            }
        }
    }
}

enum class Action {
    MUL, ADD, SAVE, OUT, JEQ, JNEQ, LT, EQ, QUIT;

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
                99 -> QUIT
                else -> error("Unknown opcode ${instruction}")
            }
        }
    }
}