package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.math.abs
import kotlin.system.measureTimeMillis

class Util {}
val utf8 = Charset.forName("UTF-8")
fun String.toInputStream() = Util::class.java.classLoader.getResourceAsStream(this)
fun InputStream.lines() = this.readBytes().toString(utf8).lines()
fun String.fileToLines(): List<String> {
    val path = "/$this"
    val res = object {}.javaClass.getResource(path)
    return File(res.toURI()).readLines()
}

fun String.fileToString(): String {
    val path = "/$this"
    return object {}.javaClass.getResource(path).readText().substringBeforeLast("\n")
}

fun <T> List<T>.permutations(): List<List<T>> {
    return if (this.size <= 1) {
        listOf(this)
    } else {
        val elementToInsert = first()
        drop(1).permutations().flatMap { permutation ->
            (0..permutation.size).map { i ->
                permutation.toMutableList().apply { add(i, elementToInsert) }
            }
        }
    }
}

suspend fun <T> Channel<T>.andSend(msg: T) : Channel<T> = this.also { send(msg) }

fun <T> List<T>.toChannel(capacity: Int = Channel.UNLIMITED): Channel<T> {
    return Channel<T>(capacity).also { this.forEach { e -> it.offer(e) }}
}

fun report(f: () -> Number) {
    var ans: Number? = null
    val timeTaken = measureTimeMillis { ans = f() }
    println("Answer [$ans] - took $timeTaken ms")
}

fun parseInstructions(instructions: String): IntArray {
    return instructions.split(",").map { it.toInt() }.toIntArray()
}

fun parseBigInstructions(instructions: String): LongArray {
    return instructions.split(",").map { it.toLong() }.toLongArray()
}
tailrec fun gcd(a: Int, b: Int): Int = if(a == 0) abs(b) else gcd(b % a, a)
tailrec fun gcd(a: Long, b: Long): Long = if(a == 0L) abs(b) else gcd(b % a, a)
fun IntArray.toMutableMap(): MutableMap<Long, Long> = this.withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap()
fun LongArray.toMutableMap(): MutableMap<Long, Long> = this.withIndex().associate { it.index.toLong() to it.value }.toMutableMap()
fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> =
    sequence {
        @Suppress("UNCHECKED_CAST")
        val pool = this as? List<T> ?: toList()
        val n = pool.size
        if(length > n) return@sequence
        val indices = IntArray(length) { it }
        while(true) {
            yield(indices.map { pool[it] })
            var i = length
            do {
                i--
                if(i == -1) return@sequence
            } while(indices[i] == i + n - length)
            indices[i]++
            for(j in i+1 until length) indices[j] = indices[j - 1] + 1
        }
    }