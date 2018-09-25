package ru.hse.spb

import ru.hse.spb.parser.ExpParser

class Scope(private val parentScope: Scope?) {
    private val varScope = HashMap<String, Int?>()
    private val funcScope = HashMap<String, Func>()

    fun addFunction(name: String, f: Func): Boolean {
        return if (funcScope.containsKey(name)) {
            false
        } else {
            funcScope[name] = f
            true
        }
    }

    fun getFunction(name: String): Func? {
        return if (funcScope.contains(name)) {
            funcScope[name]
        } else {
            parentScope?.getFunction(name)
        }
    }

    fun addVariable(name: String, x: Int?): Boolean {
        return if (varScope.containsKey(name)) {
            false
        } else {
            varScope[name] = x
            true
        }
    }

    fun setVariable(name: String, x: Int?): Boolean {
        return if (varScope.containsKey(name)) {
            varScope[name] = x
            true
        } else {
            parentScope?.setVariable(name, x) ?: false
        }
    }

    fun getVariable(name: String): Int? {
        return if (varScope.contains(name)) {
            varScope[name]
        } else {
            parentScope?.getVariable(name)
        }
    }
}


class Func(val body: (arguments: List<ExpParser.ExpressionContext>, scope: Scope) -> Int?) {
    fun call(arguments: List<ExpParser.ExpressionContext>, scope: Scope): Int? = body(arguments, scope)
}