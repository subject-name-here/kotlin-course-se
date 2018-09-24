package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser

fun main(args: Array<String>) {
    val filename = args[0]
    val expLexer = ExpLexer(CharStreams.fromFileName(filename))
    val parser = ExpParser(BufferedTokenStream(expLexer))
    val root = parser.file()
    val visitor = MyTreeVisitor(System.out)
    visitor.visitFile(root)
}