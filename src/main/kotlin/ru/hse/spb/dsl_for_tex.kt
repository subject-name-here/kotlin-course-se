package ru.hse.spb

import java.io.OutputStream
import java.lang.StringBuilder

@DslMarker
annotation class TexElementMarker

fun renderParameters(vararg parameters: String): String {
    return if (parameters.isEmpty()) "" else parameters.joinToString(", ", "[", "]")
}

infix fun String.to(value: String): String {
    return "$this=$value"
}

abstract class Element {
    abstract fun render(builder: StringBuilder)

    override fun toString(): String = buildString { render(this@buildString) }

    fun toOutputStream(out: OutputStream) {
        out.write(toString().toByteArray())
    }
}

abstract class HeadTag(private val name: String, private val argument: String, private vararg val parameters: String) : Element() {
    override fun render(builder: StringBuilder) {
        builder.append("\\$name${renderParameters(*parameters)}{$argument}\n")
    }
}

class UsePackage(argument: String, vararg parameters: String) : HeadTag("usepackage", argument, *parameters)
class DocumentClass(argument: String, vararg parameters: String) : HeadTag("documentclass", argument, *parameters)


open class TextElement(private val text: String) : Element() {
    override fun render(builder: StringBuilder) {
        builder.appendln(text)
    }
}

class Math(formulas: String) : TextElement("\$$formulas\$")


@TexElementMarker
abstract class Tag(val name: String, vararg val parameters: String) : Element() {
    val children = arrayListOf<Element>()

    override fun render(builder: StringBuilder) {
        builder.appendln("\\begin{$name}${renderParameters(*parameters)}")
        children.forEach { it.render(builder) }
        builder.appendln("\\end{$name}")
    }

    operator fun String.unaryPlus() {
        children += TextElement(this)
    }

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children += tag
        return tag
    }

    fun math(formulas: String) {
        children += Math(formulas)
    }

    fun customTag(name: String, vararg parameters: String, init: CustomTag.() -> Unit) = initTag(CustomTag(name, *parameters), init)
    fun frame(frameTitle: String, vararg parameters: String, init: Frame.() -> Unit) = initTag(Frame(frameTitle, *parameters), init)
    fun alignment(type: AlignmentType, init: Alignment.() -> Unit) = initTag(Alignment(type), init)
    fun itemize(vararg parameters: String, init: TexList.() -> Unit) = initTag(TexList("itemize", *parameters), init)
    fun enumerate(vararg parameters: String, init: TexList.() -> Unit) = initTag(TexList("enumerate", *parameters), init)
}

class CustomTag(name: String, vararg parameters: String) : Tag(name, *parameters)

class Frame(private val frameTitle: String, vararg parameters: String) : Tag("frame", *parameters) {
    override fun render(builder: StringBuilder) {
        builder.appendln("\\begin{$name}${renderParameters(*parameters)}")
        builder.appendln("\\frametitle{$frameTitle}")
        children.forEach { it.render(builder) }
        builder.appendln("\\end{$name}")
    }
}

enum class AlignmentType(val type: String) {
    LEFT("flushleft"),
    RIGHT("flushright"),
    CENTER("center")
}
class Alignment(type: AlignmentType) : Tag(type.type)

class TexList(name: String, vararg parameters: String) : Tag(name, *parameters) {
    fun item(vararg parameters: String, init: Item.() -> Unit) = initTag(Item(*parameters), init)
}

class Item(vararg parameters: String) : Tag("item", *parameters) {
    override fun render(builder: StringBuilder) {
        builder.append("\\$name${renderParameters(*parameters)} ")
        children.forEach { it.render(builder) }
    }
}


class Document(vararg parameters: String) : Tag("document", *parameters) {
    private val headers = arrayListOf<Element>()

    fun usePackage(argument: String, vararg parameters: String) {
        headers += UsePackage(argument, *parameters)
    }
    fun documentClass(argument: String, vararg parameters: String) {
        headers += DocumentClass(argument, *parameters)
    }

    override fun render(builder: StringBuilder) {
        headers.forEach { it.render(builder) }
        super.render(builder)
    }
}

fun document(vararg parameters: String, init: Document.() -> Unit) = Document(*parameters).apply(init)
