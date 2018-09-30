package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestSource {
    @Test
    fun test1() {
        val program = """
            var a = 10
            var b = 20
            if (a > b) {
                println(1)
            } else {
                println(0)
            }
        """.trimIndent()

        val baos = ByteArrayOutputStream()
        val output = PrintStream(baos)

        runInterpreter(CharStreams.fromString(program), output)

        assertEquals("0", baos.toString().trimIndent())
    }

    @Test
    fun test2() {
        val program = """
            fun fib(n) {
                if (n <= 1) {
                    return 1
                }
                return fib(n - 1) + fib(n - 2)
            }

            var i = 1
            while (i <= 5) {
                println(i, fib(i))
                i = i + 1
            }
        """.trimIndent()

        val baos = ByteArrayOutputStream()
        val output = PrintStream(baos)

        runInterpreter(CharStreams.fromString(program), output)

        val expected = """
            1 1
            2 2
            3 3
            4 5
            5 8
        """.trimIndent()

        assertEquals(expected, baos.toString().trimIndent())
    }

    @Test
    fun test3() {
        val program = """
            fun foo(n) {
                fun bar(m) {
                    return m + n
                }

                return bar(1)
            }

            println(foo(41)) // prints 42
        """.trimIndent()

        val baos = ByteArrayOutputStream()
        val output = PrintStream(baos)

        runInterpreter(CharStreams.fromString(program), output)

        val expected = "42"

        assertEquals(expected, baos.toString().trimIndent())
    }
}