package ru.hse.spb

import org.antlr.v4.runtime.*
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.PrintStream

class LexerException(s: String) : Exception(s)

class ParserException(s: String) : Exception(s)

class LexerErrorListener : BaseErrorListener() {
    override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
        throw LexerException("Lexer error on line " + line + ": " + e.toString())
    }
}

class ParserErrorListener : BaseErrorListener() {
    override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
        throw ParserException("Parser error on line " + line + ": " + e.toString())
    }
}

fun runInterpreter(input: CharStream, output: PrintStream) {
    val expLexer = ExpLexer(input)
    val parser = ExpParser(BufferedTokenStream(expLexer))

    expLexer.addErrorListener(LexerErrorListener())
    parser.addErrorListener(ParserErrorListener())

    val root = parser.file()
    val visitor = MyTreeVisitor(output)
    visitor.visitFile(root)
}

fun main(args: Array<String>) {
    val filename = args[0]
    runInterpreter(CharStreams.fromFileName(filename), System.out)
}