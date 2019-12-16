package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Day13 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val day13 = Day13()
            report { day13.part1() }
            report { day13.part2() }
        }
    }

    fun part1(): Int {
        val instructions = parseBigInstructions("day13.txt".fileToString())
        val output = IntCodeComputer(instructions.toMutableMap()).run()
        return output.chunked(3).count { (a, b, c) -> c == 2L }
    }

    fun part2(): Long = runBlocking {
        val instructions = parseBigInstructions("day13.txt".fileToString()).toMutableMap()
        instructions[0] = 2
        val input = Channel<Long>(Channel.UNLIMITED)
        val output = Channel<Long>(Channel.UNLIMITED)
        val computer = IntCodeComputer(instructions, input = input, output = output)
        val tiles = mutableMapOf<Point, Long>()
        var paddleX = 0
        val cpu = computer.launch()
        val score = launch {
            while (true) {
                try {
                    val x = output.receive()
                    val y = output.receive()
                    val p = Point(x.toInt(), y.toInt())
                    val t = output.receive()
                    tiles[p] = t
                    if (p != Point(-1, 0)) {
                        when (t) {
                            4L -> input.send(p.x.compareTo(paddleX).toLong())
                            3L -> paddleX = p.x
                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    break
                }
            }
        }
        joinAll(cpu, score)
        tiles[Point(-1, 0)] ?: -1
    }

}