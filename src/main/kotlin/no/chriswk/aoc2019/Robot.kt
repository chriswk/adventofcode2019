package no.chriswk.aoc2019

sealed class Robot {
    abstract fun turnAndMoveLeft(from: Point) : Pair<Robot, Point>
    abstract fun turnAndMoveRight(from: Point) : Pair<Robot, Point>

    object North : Robot() {
        override fun turnAndMoveLeft(from: Point): Pair<Robot, Point> = West to from.left()
        override fun turnAndMoveRight(from: Point): Pair<Robot, Point> = East to from.right()
    }
    object West : Robot() {
        override fun turnAndMoveLeft(from: Point): Pair<Robot, Point> = South to from.down()
        override fun turnAndMoveRight(from: Point): Pair<Robot, Point> = North to from.up()
    }
    object East : Robot() {
        override fun turnAndMoveLeft(from: Point): Pair<Robot, Point> = North to from.up()
        override fun turnAndMoveRight(from: Point): Pair<Robot, Point> = South to from.down()
    }
    object South :Robot() {
        override fun turnAndMoveLeft(from: Point): Pair<Robot, Point> = East to from.right()
        override fun turnAndMoveRight(from: Point): Pair<Robot, Point> = West to from.left()
    }
}