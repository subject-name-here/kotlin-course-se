package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.PrintStream

fun runInterpreter(input: CharStream, output: PrintStream) {
    val expLexer = ExpLexer(input)
    val parser = ExpParser(BufferedTokenStream(expLexer))
    val root = parser.file()
    val visitor = MyTreeVisitor(output)
    visitor.visitFile(root)
}

fun main(args: Array<String>) {
    val filename = args[0]
    runInterpreter(CharStreams.fromFileName(filename), System.out)
}