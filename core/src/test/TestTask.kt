package test

import wallhow.manvszombies.game.objects.Cell
import wallhow.manvszombies.game.objects.GameField

/**
 * Created by wallhow on 15.02.17.
 */
class TestTask {
    val tests : Array<Any>

    init {
        tests = Array(1) {
            test()
        }
    }

    fun test() {
        val gameField = GameField(400f,600f,32f)
        var free = gameField.getFreeCells(Cell.Type.T4X4)
        println("Type = ${Cell.Type.T4X4.name} count = ${free.size}")
        free.forEach(::println)


        free = gameField.getFreeCells(Cell.Type.T2X2)
        println("Type = ${Cell.Type.T2X2.name} count = ${free.size}")
        free.forEach(::println)


        free = gameField.getFreeCells(Cell.Type.T1X1)
        println("Type = ${Cell.Type.T1X1.name} count = ${free.size}")
        free.forEach(::println)


        free = gameField.getFreeCells(Cell.Type.TYPE_5)
        println("Type = ${Cell.Type.TYPE_5.name} count = ${free.size}")
        free.forEach(::println)

    }
}