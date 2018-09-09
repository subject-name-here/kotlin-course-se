package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun test1() {
        val n = 4
        val ArrayList<Vector> vs = ArrayList()
        vs.add(Vector(-1, 0, 1))
        vs.add(Vector(0, -1, 2))
        vs.add(Vector(1, 0, 3))
        vs.add(Vector(1, 1, 4))
        assertEquals(Pair(3, 4), solve(n, vs))
    }

    @Test
    fun test2() {
        val n = 6
        val ArrayList<Vector> vs = ArrayList()
        vs.add(Vector(-1, 0, 1))
        vs.add(Vector(0, -1, 2))
        vs.add(Vector(1, 0, 3))
        vs.add(Vector(1, 1, 4))
        vs.add(Vector(-4, 5, 5))
        vs.add(Vector(-4, 6, 6))
        assertEquals(Pair(5, 6), solve(n, vs))
    }
}