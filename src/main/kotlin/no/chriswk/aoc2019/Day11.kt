package no.chriswk.aoc2019

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class Day11 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day11 = Day11()
            report { day11.part1() }
            day11.part2()
        }

        val black: Long = 0L
        val white: Long = 1L
        val left: Long = 0L
        val right: Long = 1L
    }

    private val program =
        parseBigInstructions("day11.txt".fileToString()).withIndex().associate { it.index.toLong() to it.value }
            .toMutableMap()

    fun part1(): Int {
        return paintShip().size
    }

    fun part2(): Unit {
        val ship = paintShip(white)
        val min = ship.keys.minWith(Point.readOrder)!!
        val max = ship.keys.maxWith(Point.readOrder)!!
        (max.y.downTo(min.y)).forEach { y ->
            val line = (min.x..max.x).joinToString(separator = "") { x ->
                if (ship[Point(x, y)] == white) {
                    "#"
                } else {
                    " "
                }
            }
            println(line)
        }
    }

    private fun paintShip(startingColor: Long = black) = runBlocking {
        val ship = mutableMapOf(Point.ORIGIN to black)
        val computer = IntCodeComputer(program)
        coroutineScope {
            launch {
                computer.runSuspending()
            }
            launch {
                var location = Point.ORIGIN
                var robot: Robot = Robot.North
                computer.input.send(startingColor)
                while (!computer.output.isClosedForReceive) {
                    val colorMsg = computer.output.receive()
                    ship[location] = colorMsg
                    when (val dir = computer.output.receive()) {
                        left -> robot.turnAndMoveLeft(location)
                        right -> robot.turnAndMoveRight(location)
                        else -> throw IllegalArgumentException("Dont know how to turn in direction: $dir")
                    }.apply {
                        robot = first
                        location = second
                    }
                    computer.input.send(ship.getOrDefault(location, black))
                }
            }
        }
        ship
    }
}