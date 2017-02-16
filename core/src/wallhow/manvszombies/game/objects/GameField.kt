package wallhow.manvszombies.game.objects

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CPosition
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Created by wallhow on 15.02.17.
 */

class GameField ( val worldWidth: Float, val worldHeight: Float,val cellSize: Float) {
    private val collCount: Int
    private val rowCount: Int
    private var cells: Array<Array<CellDef>>
    private var cellsEntity : GdxArray<Entity>

    var paddingX = 1
    var paddingY = 1


    init {
        collCount = (worldWidth / cellSize).toInt()
        rowCount = (worldHeight / cellSize).toInt()

        cells = Array(collCount+1) { x: Int -> Array(rowCount+1) { y: Int -> CellDef(x, y) } }
        cellsEntity = GdxArray<Entity>()

        println("colls = $collCount rows = $rowCount size = ${collCount*rowCount}")

    }

    fun getFreeCells(type: Cell.Type): GdxArray<Pair<Int, Int>> {
        val cellsFree = GdxArray<Pair<Int, Int>>()
        val size = Pair(type.size.x.toInt(), type.size.y.toInt())
        val size_w = type.size.x.toInt()
        val size_h = type.size.y.toInt()

        val colls = (collCount / size_w).toInt() - 1 - paddingX
        val rows = MathUtils.ceilPositive(rowCount / size_h.toFloat()) - 1

        var x=paddingX
        var y=paddingY
        while (y<rows) {
            if(checkFree(x*size_w,y*size_h,size)) {
                cellsFree.add(Pair(x*size_w,y*size_h))
            }
            x++
            if(x>colls) {
                x=paddingX
                y++
            }
        }
        cellsFree.reverse()

        return cellsFree
    }
    fun addCell(x:Int, y:Int, func : () -> Cell) {
        val cell = func.invoke()
        cell.add(CPosition(Vector2(cellSize*x,cellSize*y)).apply { zIndex = Integer.MIN_VALUE})
        setCell(cell,x,y)
    }
    fun addCell(type: Cell.Type, count: Int,func : () -> Cell) {
        var iW = 0
        val posBig = getFreeCells(type)
        println("free cells type ${type.name} = ${posBig.size}")
        if(count < 0) {
            for (index in 0..posBig.size-1) {
                addCell(posBig[index].first,posBig[index].second,func)
            }
        }
        else {
            while (iW < count) {
                val i = MathUtils.random(1, posBig.size)
                println("selected position ${posBig[i-1]}")
                addCell(posBig[i-1].first,posBig[i-1].second,func)
                iW++
                posBig.removeIndex(i-1)
            }
        }
    }
    fun getCells(type: Cell.Type) : GdxArray<Cell> {
        val array = com.badlogic.gdx.utils.Array<Cell>()
        cellsEntity.filter { Cell.CellComponent[it].type == type }
                   .forEach { array.add(it as Cell) }

        return array
    }

    private fun setCell(cell: Cell, x: Int,y : Int) {
        val size = cell.cellComponent.type.size
        for (i in y..y+size.y.toInt()-1) {
            for (j in x..x + size.x.toInt()-1) {
                cells[j][i].set(cell)
            }
        }
        cellsEntity.add(cell)
    }
    private fun checkFree(x: Int, y: Int, size: Pair<Int, Int>): Boolean {
        var (w, h) = size
        w--
        h--
        if (!checkArrayIndexBound(x, y)) {
            return false
        }
        if (!checkArrayIndexBound(x + w, y + h)) {
            return false
        }
        if (w == 0 && h == 0) {
            if (!cells[x][y].isFree()) {
                return false
            }
        } else
        {
            var xInd = x
            var yInd = y

            while (yInd<y+h) {
                if (!cells[xInd][yInd].isFree()) {
                    return false
                }
                xInd++

                if(xInd>x+w) {
                    xInd=x
                    yInd++
                }
            }
        }

        return true
    }
    /**
     * @return true - если всё бэнчь
     */
    private fun checkArrayIndexBound(x: Int,y: Int) : Boolean {
        if(x > cells.size-1 || x< 0 || y > cells[0].size-1 || y < 0) {
            println("class GameField: Выход за пределы массива \n array[${cells.size-1}][${cells[0].size-1}]" +
                    "\nЗапрос в [$x][$y]")
            return false
        }
        return true

    }

    private class CellDef (val x: Int, val y: Int) {
        private var free : Boolean = true
        private var linkCell : Cell? = null

        fun free() {
            free = true
            linkCell = null
        }

        fun set(cell: Cell) {
            free = false
            linkCell = cell
        }

        fun isFree(): Boolean {
            return free
        }

        fun isCell(cell: Cell) : Boolean{
            if(linkCell!=null && linkCell == cell) {
                return true
            }
            return false
        }
    }

    fun removeCell(cell: Cell) {
        cells.forEach {
            it.forEach {
                if(it.isCell(cell)) {
                    it.free()
                }
            }
        }
        cellsEntity.removeValue(cell,true)
    }
}