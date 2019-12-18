package no.chriswk.aoc2019

import kotlin.math.abs
enum class Direction {
    U, D, L, R;
    fun cmd(): Long {
        return when(this) {
            U -> 1L
            D -> 2L
            L -> 3L
            R -> 4L
        }
    }
    fun back(): Long {
        return when(this) {
            U -> D.cmd()
            D -> U.cmd()
            L -> R.cmd()
            R -> L.cmd()
        }
    }
}
data class Point(val x: Int, val y: Int) {
    fun manhattan(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun route(s: String): Route {
        val route = s.split(",").asSequence().map {
            Instruction(it)
        }
        return route.fold(listOf()) { acc, (f, i) ->
            val point = if (acc.isEmpty()) {
                this
            } else {
                acc.last()
            }
            when (f) {
                Direction.U -> acc + point.up(i).toList()
                Direction.R -> acc + point.right(i).toList()
                Direction.L -> acc + point.left(i).toList()
                Direction.D -> acc + point.down(i).toList()
            }
        }
    }

    fun cardinalNeighbours(): List<Pair<Point, Direction>> {
        return Direction.values().map { this.move(it) to it }
    }
    fun move(d: Direction): Point {
        return when(d) {
            Direction.U -> this.up()
            Direction.D -> this.down()
            Direction.L -> this.left()
            Direction.R -> this.right()
        }
    }

    fun right(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(x = it.x + 1)
        }.drop(1).take(count)
    }

    fun left(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(x = it.x - 1)
        }.drop(1).take(count)
    }

    fun up(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(y = it.y + 1)
        }.drop(1).take(count)
    }

    fun down(count: Int): Sequence<Point> {
        return generateSequence(this) {
            it.copy(y = it.y - 1)
        }.drop(1).take(count)
    }

    fun dx(other: Point): Int = x - other.x
    fun dy(other: Point): Int = y - other.y
    fun up(): Point = copy(y = y + 1)
    fun down(): Point = copy(y = y - 1)
    fun left(): Point = copy(x = x - 1)
    fun right(): Point = copy(x = x + 1)

    companion object {
        val ORIGIN = Point(0, 0)
        val readOrder: Comparator<Point> = Comparator { o1, o2 ->
            when {
                o1.y != o2.y -> o1.y - o2.y
                else -> o1.x - o2.x
            }
        }
    }
}

data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun absSum() = abs(x) + abs(y) + abs(z)
    operator fun plus(b: Point3D) = Point3D(x + b.x, y + b.y, z + b.z)
    operator fun minus(b: Point3D) = Point3D(x - b.x, y - b.y, z - b.z)
    operator fun times(scale: Int) = Point3D(x*scale, y*scale, z*scale)
    fun opposite() = Point3D(-x, -y, -z)
    operator fun unaryMinus() = opposite()
    companion object {
        val ZERO = Point3D(0, 0, 0)
        fun fromString(s: String): Point3D {
            val (x, y, z) = s.replace("<", "").replace(">", "").split(",")
            val xCoord = x.split("=").last().toInt()
            val yCoord = y.split("=").last().toInt()
            val zCoord = z.split("=").last().toInt()
            return Point3D(xCoord, yCoord, zCoord)
        }
    }
    operator fun get(i: Int) = when(i) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException()
    }
}


data class Body(var location: Point3D, var velocity: Point3D) {
    fun newVelocity(otherBodies: List<Body>): Body {
        return otherBodies.fold(this) { a, nextBody ->
            a.copy(velocity = a.velocity + deltaV(nextBody.location))
        }
    }

    fun deltaV(otherBody: Point3D): Point3D {
        val dX = deltaV(location.x, otherBody.x)
        val dY = deltaV(location.y, otherBody.y)
        val dZ = deltaV(location.z, otherBody.z)
        return Point3D(dX, dY, dZ)
    }

    private fun deltaV(bodyPoint: Int, otherBody: Int): Int {
        return if (bodyPoint < otherBody) {
            1
        } else if (bodyPoint > otherBody) {
            -1
        } else {
            0
        }
    }

    fun move(): Body = this.copy(location = location + velocity)
    fun potentialEnergy(): Int = location.absSum()
    fun kineticEnergy(): Int = velocity.absSum()
    fun totalEnergy(): Int = potentialEnergy() * kineticEnergy()
}
