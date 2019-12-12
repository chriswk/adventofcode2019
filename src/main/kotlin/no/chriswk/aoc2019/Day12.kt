package no.chriswk.aoc2019

class Day12 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day12 = Day12()
            report { day12.part1() }
            report { day12.part2() }
        }
    }

    fun part1(): Int {
        val locations = parseLocations("day12.txt".fileToLines())
        val simulator = Simulator(locations)
        simulator.step(1000)
        return simulator.moons.sumBy { it.totalEnergy() }
    }

    fun part2(): Long {
        val bodies = parseLocations("day12.txt".fileToLines())
        val simulator = Simulator(bodies)
        return simulator.findCycle()
    }

    fun parseLocations(input: List<String>): List<Point3D> {
        return input.map { Point3D.fromString(it) }
    }
}

class Simulator(val positions: List<Point3D>) {
    val moons = positions.map { Body(it, Point3D.ZERO) }
    var t = 0
    fun step() {
        for ((a, b) in moons.combinations(2)) {
            val (x,y,z) = IntArray(3) { i ->
                a.location[i].compareTo(b.location[i])
            }
            val dV = Point3D(x,y,z)
            a.velocity -= dV
            b.velocity += dV
        }
        moons.forEach { it.location += it.velocity }
        t++
    }

    fun step(n: Int) { repeat(n) { step() } }
    fun findCycle(): Long {
        val cycles = IntArray(3)
        var ok: Boolean
        do {
            step()
            ok = true
            for (i in 0.until(3)) {
                if (cycles[i] != 0) {
                    continue
                }
                ok = false
                if (moons.indices.all { j ->
                        val m = moons[j]
                        m.location[i] == positions[j][i] && m.velocity[i] == 0
                    }) { cycles[i] = t }
            }
        } while (!ok)
        return cycles.fold(1L) { acc, i ->
            acc / gcd(acc, i.toLong()) * i
        }
    }
}