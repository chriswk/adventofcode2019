package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5Test {

    @Test
    fun `reads instruction correctly`() {
        val op = IntCodeInstruction.invoke(0, intArrayOf(1002))
        assertThat(op).isEqualTo(IntCodeInstruction.Multiply)
    }

    @Test
    fun `is input equal to 8 - position mode`() {
        val program = parseInstructions("3,9,8,9,10,9,4,9,99,-1,8")
        val computer = IntCodeComputer(program, mutableListOf(8))
        val equalTo8 = computer.run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer(program, mutableListOf(4)).run()
        assertThat(neqTo8).containsExactly(0)
    }

    @Test
    fun `is input less than 8 - position mode`() {
        val program = parseInstructions("3,9,7,9,10,9,4,9,99,-1,8")
        val equalTo8 = IntCodeComputer(program, mutableListOf(8)).run()
        assertThat(equalTo8).containsExactly(0)
        val neqTo8 = IntCodeComputer(program, mutableListOf(4)).run()
        assertThat(neqTo8).containsExactly(1)
    }
    @Test
    fun `is input equal to 8 - immediate mode`() {
        val equalTo8 = IntCodeComputer("3,3,1108,-1,8,3,4,3,99", mutableListOf(8)).run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer("3,3,1108,-1,8,3,4,3,99", mutableListOf(4)).run()
        assertThat(neqTo8).containsExactly(0)
    }

    @Test
    fun `is input less than 8 - immediate mode`() {
        val equalTo8 = IntCodeComputer("3,3,1107,-1,8,3,4,3,99", mutableListOf(8)).run()
        assertThat(equalTo8).containsExactly(0)
        val neqTo8 = IntCodeComputer("3,3,1107,-1,8,3,4,3,99", mutableListOf(4)).run()
        assertThat(neqTo8).containsExactly(1)
    }

    @Test
    fun `is input non-zero - position mode`() {
        val equalTo8 = IntCodeComputer("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", mutableListOf(8)).run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", mutableListOf(0)).run()
        assertThat(neqTo8).containsExactly(0)
    }
    @Test
    fun `is input non-zero - immediate mode`() {
        val equalTo8 = IntCodeComputer("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", mutableListOf(8)).run()
        assertThat(equalTo8).containsExactly(1)
        val neqTo8 = IntCodeComputer("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", mutableListOf(0)).run()
        assertThat(neqTo8).containsExactly(0)
    }

    @Test
    fun `larger example ternary`() {
        val equalTo8 = IntCodeComputer("""3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99""", mutableListOf(8)).run()
        assertThat(equalTo8).containsExactly(1000)
        val LT8 = IntCodeComputer("""3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99""", mutableListOf(0)).run()
        assertThat(LT8).containsExactly(999)
        val GT8 = IntCodeComputer("""3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99""", mutableListOf(16)).run()
        assertThat(GT8).containsExactly(1001)
    }
}