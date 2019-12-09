package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Day7(val program: IntArray) {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day7 = Day7(parseInstructions("day7.txt".fileToString()))
            report { day7.part2() }
            report { day7.part1() }
        }
    }

    fun part1(): Int {
        return listOf(0,1,2,3,4).permutations().map { run(settings = it) }.max() ?: Int.MIN_VALUE
    }
    fun part2(): Int = runBlocking {
        listOf(5,6,7,8,9).permutations().map { runAmplified(it) }.max() ?: Int.MIN_VALUE
    }

    fun run(settings: List<Int>): Int {
        return (0..4).fold(0) { prev, id ->
            val cpu = IntCodeComputer(program = program.copyOf(), inputs = mutableListOf(settings[id], prev))
            cpu.run().lastOrNull() ?: Int.MIN_VALUE
        }
    }

    suspend fun runAmplified(settings: List<Int>) = coroutineScope {
        val a = IntCodeComputer(program.copyOf(), listOf(settings[0], 0).toChannel())
        val b = IntCodeComputer(program.copyOf() ,listOf(settings[1]).toChannel())
        val c = IntCodeComputer(program.copyOf(), listOf(settings[2]).toChannel())
        val d = IntCodeComputer(program.copyOf(), listOf(settings[3]).toChannel())
        val e = IntCodeComputer(program.copyOf(), listOf(settings[4]).toChannel())
        val channelSpy = ChannelSpy(e.output, a.input)
        coroutineScope {
            launch { channelSpy.listen() }
            launch { a.runSuspending() }
            launch { b.runSuspending() }
            launch { c.runSuspending() }
            launch { d.runSuspending() }
            launch { e.runSuspending() }
        }
        channelSpy.spy.receive()
    }

}

class ChannelSpy<T>(val input: Channel<T>, val output: Channel<T>, val spy: Channel<T> = Channel(Channel.CONFLATED)) {
    suspend fun listen() = coroutineScope {
        for(received in input) {
            spy.send(received)
            output.send(received)
        }
    }
}