package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestException {
    private fun runProgram(program: String): String {
        val baos = ByteArrayOutputStream()
        val output = PrintStream(baos)

        runInterpreter(CharStreams.fromString(program), output)

        return baos.toString().trimIndent()
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testVariableError1() {
        val program = """
            var x
            println(x)
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testVariableError2() {
        val program = """
            println(x)
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testVariableError3() {
        val program = """
            var x = 2
            var x
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testScopeError1() {
        val program = """
            if (2 > 1) {
                var i = 5
            }
            println(i)
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testScopeError2() {
        val program = """
            fun foo() {
                fun bar(x, y, z) {
                    println(x * y * z)
                }
            }

            bar(5, 6, 7)
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testScopeError3() {
        val program = """
            foo()
            fun foo() {}
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testScopeError4() {
        val program = """
            fun foo() {}
            fun foo() {}
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testDivisionError1() {
        val program = """
            var x = 5 / 0
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testDivisionError2() {
        val program = """
            var y = 0
            var x = 5 % y
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testDivisionError3() {
        val program = """
            fun mod(x, y) {
                return x % y
            }
            println(mod(2, 0))
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testAssignemntError1() {
        val program = """
            x = 5
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testAssignemntError2() {
        val program = """
            fun foo() {
                var x = 5
                println(x)
            }
            x = 3
            foo()
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testFunctionError1() {
        val program = """
            fun foo() {
                println(42)
            }
            var x = 3
            foo(x)
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testFunctionError2() {
        val program = """
            fun foo(x) {
                println(x)
            }
            foo()
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = MyTreeVisitorException::class)
    fun testFunctionError3() {
        val program = """
            fun foo() {
                println(22)
            }
            var y = foo + 2
        """.trimIndent()

        runProgram(program)
    }


    @Test(expected = LexerException::class)
    fun testLexerError1() {
        val program = """
            var a = 5 ~ 2
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = LexerException::class)
    fun testLexerError2() {
        val program = """
            var Ж = 5
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = LexerException::class)
    fun testLexerError3() {
        val program = """
            var x := 5
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError1() {
        val program = """
            var x = 2
            x <> 5
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError2() {
        val program = """
            var if = 5
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError3() {
        val program = """
            foo() {
                println(5)
            }
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError4() {
        val program = """
            var x = ((2 + 2) || (2 - 2)
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError5() {
        val program = """
            var x = 2
            x *= 3
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError6() {
        val program = """
            fun foo() {
                return
            }
        """.trimIndent()

        runProgram(program)
    }

    @Test(expected = ParserException::class)
    fun testParserError7() {
        val program = """
            var x = +5
        """.trimIndent()

        runProgram(program)
    }

}