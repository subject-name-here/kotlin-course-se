package ru.hse.spb

import ru.hse.spb.parser.ExpParser

class Scope() {
    val varScope = HashMap<String, Int?>()
    val funcScope = HashMap<String, Func>()

    constructor(scope: Scope) : this() {
        varScope.putAll(scope.varScope)
        funcScope.putAll(scope.funcScope)
    }
}


class Func(val parameters: List<String>, val body: (arguments: List<ExpParser.ExpressionContext>, scope: Scope) -> Int?) {
    fun call(arguments: List<ExpParser.ExpressionContext>, scope: Scope): Int? {
        return body(arguments, scope)
    }
}