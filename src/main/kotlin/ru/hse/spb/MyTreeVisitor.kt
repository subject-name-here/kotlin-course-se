package ru.hse.spb

import ru.hse.spb.parser.ExpBaseVisitor
import ru.hse.spb.parser.ExpParser
import java.io.PrintStream

private fun Boolean.toInt() = if (this) 1 else 0

class MyTreeVisitorException(s: String?) : Exception(s)

class MyTreeVisitor(private val outStream: PrintStream) : ExpBaseVisitor<Void>() {

    override fun visitFile(ctx: ExpParser.FileContext?): Void? {
        val main = ctx?.block()
        val scope = Scope()
        scope.funcScope["println"] = Func(ArrayList()) { args: List<ExpParser.ExpressionContext>, outerScope: Scope ->
            for (arg in args) {
                outStream.println(visitExpression(arg, outerScope))
            }
            null
        }

        if (main != null)
            visitBlock(main, scope)

        return null
    }

    fun visitBlock(ctx: ExpParser.BlockContext, scope: Scope): Int? {
        val innerScope = Scope(scope)
        for (statement in ctx.statement()) {
            val res = visitStatement(statement, innerScope)
            if (res != null)
                return res
        }

        return null
    }

    fun visitBlockWithBraces(ctx: ExpParser.BlockWithBracesContext, scope: Scope): Int? {
        return visitBlock(ctx.block(), scope)
    }

    fun visitStatement(ctx: ExpParser.StatementContext, scope: Scope): Int? {
        if (ctx.assignment() != null) {
            visitAssignment(ctx.assignment(), scope)
        } else if (ctx.expression() != null) {
            visitExpression(ctx.expression(), scope)
        } else if (ctx.function() != null) {
            visitFunction(ctx.function(), scope)
        } else if (ctx.ifExpr() != null) {
            return visitIfExpr(ctx.ifExpr(), scope)
        } else if (ctx.returnExpr() != null) {
            return visitReturnExpr(ctx.returnExpr(), scope)
        } else if (ctx.variable() != null) {
            visitVariable(ctx.variable(), scope)
        } else if (ctx.whileExpr() != null) {
            visitWhileExpr(ctx.whileExpr(), scope)
        }

        return null
    }

    fun visitFunction(ctx: ExpParser.FunctionContext, scope: Scope) {
        val name = ctx.Identifier().text
        if (name in scope.funcScope) {
            throw MyTreeVisitorException("Line " + ctx.Identifier().symbol.line + ": function already defined.")
        }

        val parameters = ArrayList<String>()
        for (x in ctx.parameterNames().Identifier()) {
            parameters.add(x.text)
        }

        val body = ctx.blockWithBraces()

        val lambda = { args: List<ExpParser.ExpressionContext>, outerScope: Scope ->
            if (args.size != parameters.size) {
                throw MyTreeVisitorException("Line " + ctx.Identifier().symbol.line + ": not enough parameters.")
            }

            val innerScope = Scope(outerScope)
            for (i in 0 until parameters.size) {
                innerScope.varScope[parameters[i]] = visitExpression(args[i], outerScope)
            }

            visitBlockWithBraces(body, innerScope)
        }
        scope.funcScope[name] = Func(parameters, lambda)
    }

    fun visitFunctionCall(ctx: ExpParser.FunctionCallContext, scope: Scope): Int {
        val name = ctx.Identifier().text
        if (name !in scope.funcScope) {
            throw MyTreeVisitorException("Line " + ctx.Identifier().symbol.line + ": function is not defined.")
        }
        val f = scope.funcScope[name]

        val args = ctx.arguments().expression()
        return f?.call(args, scope) ?: 0
    }

    fun visitVariable(ctx: ExpParser.VariableContext, scope: Scope) {
        val name = ctx.Identifier().text
        if (name in scope.varScope) {
            throw MyTreeVisitorException("Line " + ctx.Identifier().symbol.line + ": variable already defined.")
        }

        scope.varScope[name] = if (ctx.expression() == null) null else visitExpression(ctx.expression(), scope)
    }

    fun visitAssignment(ctx: ExpParser.AssignmentContext, scope: Scope) {
        val name = ctx.Identifier().text
        if (name !in scope.varScope) {
            throw MyTreeVisitorException("Line " + ctx.Identifier().symbol.line + ": variable is not defined.")
        }

        val value = visitExpression(ctx.expression(), scope)
        scope.varScope[name] = value
    }

    fun visitWhileExpr(ctx: ExpParser.WhileExprContext, scope: Scope): Int? {
        val cond = ctx.expression()
        while (visitExpression(cond, scope)!= 0) {
            val res = visitBlockWithBraces(ctx.blockWithBraces(), scope)
            if (res != null)
                return res
        }

        return null
    }

    fun visitIfExpr(ctx: ExpParser.IfExprContext, scope: Scope): Int? {
        val condResult = visitExpression(ctx.expression(), scope)

        return visitBlockWithBraces(if (condResult != 0) ctx.trueBlock else ctx.falseBlock, scope)
    }

    fun visitReturnExpr(ctx: ExpParser.ReturnExprContext, scope: Scope): Int? {
        return visitExpression(ctx.expression(), scope)
    }

    fun visitExpression(ctx: ExpParser.ExpressionContext, scope: Scope): Int {
        if (ctx.atomExpression() != null) {
            return visitAtomExpression(ctx.atomExpression(), scope)
        }

        val left = visitExpression(ctx.left, scope)
        val right = visitExpression(ctx.right, scope)

        return when (ctx.op.text) {
            "||" -> (left != 0 || right != 0).toInt()
            "&&" -> (left != 0 && right != 0).toInt()
            "==" -> (left == right).toInt()
            "!=" -> (left != right).toInt()
            ">=" -> (left >= right).toInt()
            "<=" -> (left <= right).toInt()
            ">" -> (left > right).toInt()
            "<" -> (left < right).toInt()
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> if (right != 0) left / right
                else throw MyTreeVisitorException("Line"  + ctx.op.line + ": divide by zero.")
            "%" -> if (right != 0) left % right
                else throw MyTreeVisitorException("Line"  + ctx.op.line + ": divide by zero.")

            else -> throw MyTreeVisitorException("Line " + ctx.op.line + ": operation unknown error.")
        }
    }

    fun visitAtomExpression(ctx: ExpParser.AtomExpressionContext, scope: Scope): Int {
        if (ctx.Identifier() != null) {
            val name = ctx.Identifier().text
            val value = scope.varScope[name]

            if (value != null) {
                return value
            } else {
                throw MyTreeVisitorException("Line " + ctx.Identifier().symbol.line + ": variable is not defined.")
            }
        } else if (ctx.expression() != null) {
            return visitExpression(ctx.expression(), scope)
        } else if (ctx.functionCall() != null) {
            return visitFunctionCall(ctx.functionCall(), scope)
        } else {
            return ctx.Literal().text.toInt()
        }
    }

}