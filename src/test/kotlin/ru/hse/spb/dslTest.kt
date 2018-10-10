package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class dslTest {

    @Test
    fun test1() {
        val rows = arrayListOf<String>("lalala", "it goes", "around the world")

        val actual = document {
            documentClass("beamer", "12pt", "handout", "notes" to "show")
            usePackage("babel", "russian")
            frame("frametitle", "arg1" to "arg2", "arg3 = arg54") {
                itemize {
                    for (row in rows) {
                        item { +"$row text" }
                    }
                }

                customTag("pyglist", "language" to "kotlin") {
                    +"""
                       |val a = 1
                       |
                       |"""
                }
            }
        }.toString().trimIndent().trimMargin()

        val expected = """|\documentclass[12pt, handout, notes=show]{beamer}
        |\usepackage[russian]{babel}
        |\begin{document}
        |\begin{frame}[arg1=arg2, arg3 = arg54]
        |\frametitle{frametitle}
        |\begin{itemize}
        |\item lalala text
        |\item it goes text
        |\item around the world text
        |\end{itemize}
        |\begin{pyglist}[language=kotlin]
        |
                       |val a = 1
                       |
        |
        |\end{pyglist}
        |\end{frame}
        |\end{document}
        """.trimMargin()
        assertEquals(expected, actual)
    }

    @Test
    fun test2() {
        val actual = document {
            documentClass("beamer")
            usePackage("babel", "russian", "english")
            alignment(AlignmentType.RIGHT) {
                enumerate {
                    item { +"1" }
                    item { +"2" }
                    item { +"3" }
                }
            }
            + "end of line"

            math("a^2 + b^2 = 112 \\forall b")
        }.toString().trimIndent()

        val expected = """|\documentclass{beamer}
        |\usepackage[russian, english]{babel}
        |\begin{document}
        |\begin{flushright}
        |\begin{enumerate}
        |\item 1
        |\item 2
        |\item 3
        |\end{enumerate}
        |\end{flushright}
        |end of line
        |${"$"}a^2 + b^2 = 112 \forall b${"$"}
        |\end{document}
        """.trimIndent().trimMargin()
        assertEquals(expected, actual)
    }
}