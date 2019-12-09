package no.chriswk.aoc2019

class IntCodeComputer(val program: Array<Int>) {
    fun run(startIdx: Int = 0, inputs: List<Int> = emptyList()): Pair<Array<Int>, List<Int>> {
        val inputsCopy = inputs.toMutableList()
        val outputs = mutableListOf<Int>()
        val memory = program.copyOf()
        var ip = startIdx
        loop@while (true) {
            val op = IntCodeOp.fromInt(memory[ip])
            when(op.action) {
                Action.ADD -> {
                    val (left, right) = getTwo(memory, op, ip+1, ip+2)
                    assign(memory, op.param3Mode, ip+3, left+right)
                    ip += 4
                }
                Action.MUL -> {
                    val (left, right) = getTwo(memory, op, ip+1, ip+2)
                    assign(memory, op.param3Mode, ip+3, left*right)
                    ip +=4
                }
                Action.SAVE -> {
                    assign(memory, op.param1Mode, ip+1, newVal = inputsCopy.removeAt(0))
                    ip += 2
                }
                Action.OUT -> {
                    outputs.add(get(memory, op.param1Mode, ip+1))
                    ip += 2
                }
                Action.JEQ -> {
                    val (left, right) = getTwo(memory, op, ip+1, ip+2)
                    if (left != 0) {
                        ip = right
                    } else {
                        ip += 3
                    }
                }
                Action.JNEQ -> {
                    val (left, right) = getTwo(memory, op, ip+1, ip+2)
                    if (left == 0) {
                        ip = right
                    } else {
                        ip += 3
                    }
                }
                Action.LT -> {
                    val (left, right) = getTwo(memory, op, ip+1, ip+2)
                    val newVal = if (left < right) {
                        1
                    } else {
                        0
                    }
                    assign(memory, op.param3Mode, ip+3, newVal)
                    ip += 4
                }
                Action.EQ -> {
                    val (left, right) = getTwo(memory, op, ip+1, ip+2)
                    val newVal = if (left == right) {
                        1
                    } else {
                        0
                    }
                    assign(memory, op.param3Mode, ip+3, newVal)
                    ip += 4
                }
                Action.QUIT -> {
                    println("Terminating")
                    break@loop
                }
            }
        }
        return memory to outputs
    }
    fun get(memory: Array<Int>, mode: Mode, address: Int): Int {
        return when(mode) {
            Mode.IMMEDIATE -> memory[address]
            Mode.POSITION -> memory[memory[address]]
        }
    }
    fun getTwo(memory: Array<Int>, op: IntCodeOp, address1: Int, address2: Int): Pair<Int, Int> {
        val left = when(op.param1Mode) {
            Mode.IMMEDIATE -> memory[address1]
            Mode.POSITION -> memory[memory[address1]]
        }
        val right = when(op.param2Mode) {
            Mode.IMMEDIATE -> memory[address2]
            Mode.POSITION -> memory[memory[address2]]
        }
        return left to right
    }

    fun assign(memory: Array<Int>, mode: Mode, address: Int, newVal: Int) {
        when(mode) {
            Mode.IMMEDIATE -> memory[address] = newVal
            Mode.POSITION -> memory[memory[address]] = newVal
        }
    }

    companion object {
        fun parseInstructions(instructions: String): Array<Int> {
            return instructions.split(",").map { it.toInt() }.toTypedArray()
        }
        fun parse(instructions: String): IntCodeComputer {
            return IntCodeComputer(program = parseInstructions(instructions))
        }
    }
}
data class IntCodeOp(val action: Action, val param1Mode: Mode, val param2Mode: Mode, val param3Mode: Mode) {
    companion object {
        fun fromInt(instruction: Int): IntCodeOp {
            val action = Action.fromInt(instruction % 100)
            val param1Mode = Mode.valueOf(instruction % 1000 / 100)
            val param2Mode = Mode.valueOf(instruction % 10000 / 1000)
            val param3Mode = Mode.valueOf(instruction % 100000 / 10000)
            return IntCodeOp(action = action, param1Mode = param1Mode, param2Mode = param2Mode, param3Mode = param3Mode)
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