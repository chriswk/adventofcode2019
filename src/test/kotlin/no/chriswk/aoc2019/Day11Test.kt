package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Day11Test {
    @ParameterizedTest
    @MethodSource("turnLeft")
    fun `Handles turning left and moving`(facing: Robot, facingAfterTurn: Robot, endAtPoint: Point) {
        val (h, p) = facing.turnAndMoveLeft(Point.ORIGIN)
        assertThat(p).isEqualTo(endAtPoint)
        assertThat(h).isEqualTo(facingAfterTurn)
    }
    @ParameterizedTest
    @MethodSource("turnRight")
    fun `Handles turning right and moving`(facing: Robot, facingAfterTurn: Robot, endAtPoint: Point) {
        val (h, p) = facing.turnAndMoveRight(Point.ORIGIN)
        assertThat(p).isEqualTo(endAtPoint)
        assertThat(h).isEqualTo(facingAfterTurn)
    }

    companion object {
        @JvmStatic
        fun turnLeft(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Robot.North, Robot.West, Point(-1, 0)),
                Arguments.of(Robot.West, Robot.South, Point(0, -1)),
                Arguments.of(Robot.South, Robot.East, Point(1, 0)),
                Arguments.of(Robot.East, Robot.North, Point(0, 1))
            )
        }

        @JvmStatic
        fun turnRight(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Robot.North, Robot.East, Point(1, 0)),
                Arguments.of(Robot.West, Robot.North, Point(0, 1)),
                Arguments.of(Robot.South, Robot.West, Point(-1, 0)),
                Arguments.of(Robot.East, Robot.South, Point(0, -1))
            )
        }

    }
}