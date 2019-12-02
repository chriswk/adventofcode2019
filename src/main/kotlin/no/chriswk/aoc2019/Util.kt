package no.chriswk.aoc2019
class Util {}
fun String.toInputStream() = Util::class.java.classLoader.getResourceAsStream(this)