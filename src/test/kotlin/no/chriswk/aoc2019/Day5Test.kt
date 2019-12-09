package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5Test {

    @Test
    fun `reads instruction correctly`() {
        val op = IntCodeOp.fromInt(1002)
        assertThat(op.action).isEqualTo(Action.MUL)
        assertThat(op.param1Mode).isEqualTo(Mode.POSITION)
        assertThat(op.param2Mode).isEqualTo(Mode.IMMEDIATE)
        assertThat(op.param3Mode).isEqualTo(Mode.POSITION)
    }

    @Test
    fun `is input equal to 8 - position mode`() {
        val computer = IntCodeComputer.prepare("3,9,8,9,10,9,4,9,99,-1,8", listOf(8))
        val equalTo8 = computer.run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer.prepare("3,9,8,9,10,9,4,9,99,-1,8", listOf(4)).run()
        assertThat(neqTo8).containsExactly(0)
    }

    @Test
    fun `is input less than 8 - position mode`() {
        val equalTo8 = IntCodeComputer.prepare("3,9,7,9,10,9,4,9,99,-1,8", listOf(8)).run()
        assertThat(equalTo8).containsExactly(0)
        val neqTo8 = IntCodeComputer.prepare("3,9,7,9,10,9,4,9,99,-1,8", listOf(4)).run()
        assertThat(neqTo8).containsExactly(1)
    }
    @Test
    fun `is input equal to 8 - immediate mode`() {
        val equalTo8 = IntCodeComputer.prepare("3,3,1108,-1,8,3,4,3,99", listOf(8)).run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer.prepare("3,3,1108,-1,8,3,4,3,99", listOf(4)).run()
        assertThat(neqTo8).containsExactly(0)
    }

    @Test
    fun `is input less than 8 - immediate mode`() {
        val equalTo8 = IntCodeComputer.prepare("3,3,1107,-1,8,3,4,3,99", listOf(8)).run()
        assertThat(equalTo8).containsExactly(0)
        val neqTo8 = IntCodeComputer.prepare("3,3,1107,-1,8,3,4,3,99", listOf(4)).run()
        assertThat(neqTo8).containsExactly(1)
    }

    @Test
    fun `is input non-zero - position mode`() {
        val equalTo8 = IntCodeComputer.prepare("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", listOf(8)).run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer.prepare("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", listOf(0)).run()
        assertThat(neqTo8).containsExactly(0)
    }
    @Test
    fun `is input non-zero - immediate mode`() {
        val equalTo8 = IntCodeComputer.prepare("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", listOf(8)).run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer.prepare("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", listOf(0)).run()
        assertThat(neqTo8).containsExactly(0)
    }

    @Test
    fun `larger example ternary`() {
        val equalTo8 = IntCodeComputer.prepare("""3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99""", listOf(8)).run()
        assertThat(equalTo8).containsExactly(1000)
        val LT8 = IntCodeComputer.prepare("""3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99""", listOf(0)).run()
        assertThat(LT8).containsExactly(999)
        val GT8 = IntCodeComputer.prepare("""3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99""", listOf(16)).run()
        assertThat(GT8).containsExactly(1001)
    }
}