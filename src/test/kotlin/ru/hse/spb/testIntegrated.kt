package ru.hse.spb

import org.antlr.v4.runtime.CharStreams
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestIntegrated {
    private fun runProgram(program: String): String {
        val baos = ByteArrayOutputStream()
        val output = PrintStream(baos)

        runInterpreter(CharStreams.fromString(program), output)

        return baos.toString().trimIndent()
    }

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

        assertEquals("0", runProgram(program))
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

        val expected = """
            1 1
            2 2
            3 3
            4 5
            5 8
        """.trimIndent()

        assertEquals(expected, runProgram(program))
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

        val expected = "42"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun test4() {
        val program = """
            fun foo(n) {
                fun foo(m) {
                    return m + n
                }

                return foo(2)
            }

            println(foo(4))
        """.trimIndent()

        val expected = "6"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun test5() {
        val program = """
            var x = 2
            fun foo(n) {
                return n * x
            }

            println(foo(4))
        """.trimIndent()

        val expected = "8"

        assertEquals(expected, runProgram(program))
    }


    @Test
    fun testPrintln1() {
        val program = "println(22229)"

        val expected = "22229"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testPrintln2() {
        val program = "println(2+2)"

        val expected = "4"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testPrintln3() {
        val program = "println(2,3,4,5,6,7)"

        val expected = "2 3 4 5 6 7"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testVariable1() {
        val program = """
            var x = 22
            println(x)
        """.trimIndent()

        val expected = "22"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testVariable2() {
        val program = """
            var x
            x = 22
            println(x)
        """.trimIndent()

        val expected = "22"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testIf1() {
        val program = """
            if (20 > 10) {
                println(1)
            } else {
                println(0)
            }
        """.trimIndent()

        val expected = "1"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testIf2() {
        val program = """
            if (20 > 10) {
                println(1)
            }
        """.trimIndent()

        val expected = "1"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testIf3() {
        val program = """
            if (10 > 20) {
                println(1)
            }
        """.trimIndent()

        val expected = ""

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testIf4() {
        val program = """
            if (20 < 10) {
                println(1)
            } else {
                println(0)
            }
        """.trimIndent()

        val expected = "0"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testWhile1() {
        val program = """
            var a = 3
            while (a < 5) {
                a = a + 1
                println(a)
            }
        """.trimIndent()

        val expected = """
            4
            5
        """.trimIndent()

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testWhile2() {
        val program = """
            var a = 3
            while (a > 5) {
                a = a + 1
                println(a)
            }
        """.trimIndent()

        val expected = ""

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testAssignment1() {
        val program = """
            var a = 3
            var b
            a = 5
            println(a)
            b = 10
            println(b)
        """.trimIndent()

        val expected = """
            5
            10
        """.trimIndent()

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testAssignment2() {
        val program = """
            var a = 3
            var b = 3
            if (2 > 1) {
                a = 5
            } else {
                b = 5
            }
            println(a, b)
        """.trimIndent()

        val expected = "5 3"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunction1() {
        val program = """
            fun foo() {
                println(42)
            }
            foo()
        """.trimIndent()

        val expected = "42"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunction2() {
        val program = """
            fun foo() {
                println(42)
            }

            fun bar(x) {
                println(x)
                foo()
            }

            bar(134)
        """.trimIndent()

        val expected = """
            134
            42
        """.trimIndent()

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunction3() {
        val program = """
            fun foo() {
                fun bar(x, y, z) {
                    println(x * y * z)
                }

                bar(5, 6, 7)
            }

            foo()
        """.trimIndent()

        val expected = "210"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunction4() {
        val program = """
            fun foo() {
                println(42)
            }
            fun bar() {
                foo()
            }
            bar()
        """.trimIndent()

        val expected = "42"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testReturn1() {
        val program = """
            println(1)
            return 0
            println(2)
        """.trimIndent()

        val expected = "1"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testReturn2() {
        val program = """
            fun fic(x) {
                if (x == 2) {
                    println(0)
                    return 0
                }

                println(22229)
            }
            fic(2)
            println(1)

        """.trimIndent()

        val expected = """
            0
            1
        """.trimIndent()

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testReturn3() {
        val program = """
            fun fic(x, y, z) {
                if (x == 6) {
                    while (x > 0) {
                        if (y == 6) {
                            if (z == 6) {
                                return 0
                            }
                        }
                        x = 0
                    }
                }

                println(22229)
            }

            fic(6, 6, 6)
        """.trimIndent()

        val expected = ""

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunctionCall1() {
        val program = """
            fun foo() {
                return 42
            }
            println(foo())
        """.trimIndent()

        val expected = "42"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunctionCall2() {
        val program = """
            fun foo(x) {
                return x * 2
            }
            println(foo(2), foo(3) + foo(4))
        """.trimIndent()

        val expected = "4 14"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunctionCall3() {
        val program = """
            fun foo(x) {
                return x * x + 1
            }
            var t = foo(foo(foo(2)))
            println(t)
        """.trimIndent()

        val expected = "677"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testFunctionCall4() {
        val program = """
            fun foo(x) {}
            var t = foo(2)
            println(t)
        """.trimIndent()

        val expected = "0"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testExpression1() {
        val program = """
            var x = 2 + 2 * 2
            var y = (2+2) * 2
            println(x, y)
        """.trimIndent()

        val expected = "6 8"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testExpression2() {
        val program = """
            var x = 2 + 2 * 2
            var y = (2+2) * 2
            println(x, y)
        """.trimIndent()

        val expected = "6 8"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testExpression3() {
        val program = """
            var x = 5
            var b = x >= 3 || x > 0 && x < 2
            println(b)
        """.trimIndent()

        val expected = "1"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testExpression4() {
        val program = """
            var x = 5
            var b = (x <= 10) + (x != 2)
            println(b)
        """.trimIndent()

        val expected = "2"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testExpression5() {
        val program = """
            var x = 100
            var b = x / 15 + x % 15
            println(b)
        """.trimIndent()

        val expected = "16"

        assertEquals(expected, runProgram(program))
    }

    @Test
    fun testExpression6() {
        val program = """
            var x = -5
            println(x)
        """.trimIndent()

        val expected = "-5"

        assertEquals(expected, runProgram(program))
    }

}