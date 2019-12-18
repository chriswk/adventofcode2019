package no.chriswk.aoc2019

import kotlin.math.ceil
import kotlin.math.sign

class Day14(val cost : Map<String, Pair<Long, List<Pair<Long, String>>>> = parseInput("day14.txt".fileToLines())) {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day14 = Day14()
            report { day14.part1() }
            report { day14.part2() }
        }
        fun parseInput(input: List<String>): Map<String, Pair<Long, List<Pair<Long, String>>>> {
            return input.map { row ->
                val split: List<String> = row.split(" => ")
                val left: List<Pair<Long, String>> = split.first().split(",")
                    .map { it.trim() }
                    .map {
                        it.split(" ").let { r ->
                            Pair(r.first().toLong(), r.last())
                        }
                    }
                val (amount, type) = split.last().split(" ")
                type to Pair(amount.toLong(), left)
            }.toMap()
        }

    }

    fun part1() : Long {
        return calculateCost()
    }

    fun part2(): Long {
        val oneTrillion = 1_000_000_000_000L
        return binarySearch(0L..oneTrillion) { oneTrillion.compareTo(calculateCost(amountDesired = it)) }
    }

    fun binarySearch(range: LongRange, fn: (Long) -> Int): Long {
        var low = range.first
        var high = range.last
        while (low <= high) {
            val mid = (low + high) / 2
            when(fn(mid).sign) {
                -1 -> high = mid -1
                1 -> low = mid + 1
                0 -> return mid
            }
        }
        return low - 1
    }

    fun calculateCost(material: String = "FUEL", amountDesired: Long = 1L, inventory: MutableMap<String, Long> = mutableMapOf()): Long {
        return if (material == "ORE") {
            amountDesired
        } else {
            val quant = inventory.getOrDefault(material, 0)
            val need = if (quant > 0) {
                inventory[material] = (quant - amountDesired).coerceAtLeast(0)
                amountDesired - quant
            } else {
                amountDesired
            }
            if (need > 0) {
                val recipe = cost.getValue(material)
                val iterations = ceil(need.toDouble() / recipe.first).toInt()
                val produced = recipe.first * iterations
                if (need < produced) {
                    inventory[material] = inventory.getOrDefault(material, 0) + produced - need
                }
                val mats = recipe.second.map { calculateCost(it.second, it.first * iterations, inventory) }
                mats.sum()
            } else {
                0
            }
        }
    }

}