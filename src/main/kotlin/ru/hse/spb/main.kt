package ru.hse.spb

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sign

fun pseudoscalarProduct(v1: Vector, v2: Vector) = v1.x * v2.y - v1.y * v2.x

fun scalarProduct(v1: Vector, v2: Vector) = v1.x * v2.x + v1.y * v2.y

class Vector(val x: Int, val y: Int, val num: Int) : Comparable<Vector> {
    override fun compareTo(other: Vector): Int {
        val quadrantDiff = getQuadrant() - other.getQuadrant()

        return if (quadrantDiff != 0)
            quadrantDiff
        else
            -pseudoscalarProduct(this, other)
    }

    private fun getQuadrant(): Int {
        return when {
            x > 0 && y >= 0 -> 1
            x <= 0 && y > 0 -> 2
            x < 0 && y <= 0 -> 3
            x >= 0 && y < 0 -> 4
            else -> 0
        }
    }
}

class Angle(val v1: Vector, val v2: Vector) : Comparable<Angle> {
    override fun compareTo(other: Angle): Int {
        val s1 = scalarProduct(v1, v2).toLong()
        val p1 = -pseudoscalarProduct(v1, v2).toLong()

        val s2 = scalarProduct(other.v1, other.v2).toLong()
        val p2 = -pseudoscalarProduct(other.v1, other.v2).toLong()

        return (s1 * p2 - s2 * p1).sign
    }
}

fun solve(n: Int, vectors: ArrayList<Vector>): Pair<Int, Int> {
    vectors.sort()

    val angles : ArrayList<Angle> = ArrayList()
    for (i in 0 until n) {
        val p = pseudoscalarProduct(vectors[i], vectors[(i + 1) % n])
        if (p < 0)
            angles.add(Angle(vectors[i], vectors[(i + 1) % n]))
        else
            angles.add(Angle(vectors[(i + 1) % n], vectors[i]))
    }

    angles.sort()
    return Pair(angles.last().v1.num, angles.last().v2.num)
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt()

    val vectors: ArrayList<Vector> = ArrayList()
    for (i in 0 until n) {
        val x = input.nextInt()
        val y = input.nextInt()
        vectors.add(Vector(x, y, i + 1))
    }

    val ans = solve(n, vectors)

    println("${ans.first} ${ans.second}")
}
