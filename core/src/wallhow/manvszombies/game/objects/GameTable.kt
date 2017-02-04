package wallhow.manvszombies.game.objects

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.sun.org.apache.xpath.internal.operations.Bool
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CHealth

/**
 * Created by wallhow on 22.01.17.
 */
class GameTable ( worldWidth: Float, worldHeight: Float) {
    val width: Int
    val height: Int
    val defCellSize = 40f


    val cells : Cells
    init {
        width = (worldWidth / defCellSize).toInt()
        height = (worldHeight / defCellSize).toInt()

        cells = Cells(width,height,defCellSize)

        add(Cell.Type.T4X4,2)
        add(Cell.Type.T2X2,4)
        add(Cell.Type.T1X1,3)
        //add(Cell.Type.T2X2,-1)
        //add(Cell.Type.T1X1,-1)

        cells.cells.forEach {
            Game.engine.addEntity(it)
        }

    }

    fun add(type: Cell.Type, count: Int) {
        var iW = 0
        val posBig = cells.getCellFree(type)
        if(count < 0) {
            for (index in 0..posBig.size-1) {
                cells.addCell(type, posBig[index])
            }
        }
        else {
            while (iW < count) {
                val i = MathUtils.random(0, posBig.size - 1)
                cells.addCell(type, posBig[i])
                iW++
                posBig.removeIndex(i)
            }
        }
    }

    class Cells (val coll: Int,val row: Int,val cellSizePx:Float) {
        val paddingY = 1
        val paddingX = 1
        val cells : Array<Cell>
        val cellTexture = Texture("assets/cell.png")
        private val cellsFlags : kotlin.Array<kotlin.Array<Boolean>>

        init {
            cells = Array()
            cellsFlags = Array(coll) { Array(row) {true} }

        }

        private fun add(cell: Cell, x:Int, y:Int) {
            val sizeX = cell.cellComponent.type.size.x
            val sizeY = cell.cellComponent.type.size.y
            if (cellsFlags[x][y]) {

                cells.add(cell)
                for (xA in paddingX..sizeX.toInt()-1-paddingX) {
                    for (yA in paddingY..sizeY.toInt()-1) {
                        cellsFlags[x+xA][y + yA] = false
                    }
                }

            }

        }
        fun addCell(type: Cell.Type, x:Float, y:Float) {
            val cell = Cell(type)
            val size = type.size
            cell.add(CPosition(Vector2(cellSizePx*x,cellSizePx*y)).apply { zIndex = Integer.MIN_VALUE})
            cell.add(CImage(TextureRegion(cellTexture),cellSizePx*size.x,cellSizePx*size.y))

            add(cell,x.toInt(),y.toInt())
        }
        fun addCell(type: Cell.Type, position: Vector2) {
            addCell(type,position.x,position.y)
        }
        fun getCellFree(type: Cell.Type) : Array<Vector2> {
            val pos = Array<Vector2>()

            for (x in paddingX..MathUtils.ceilPositive(coll / type.size.x) -1 -paddingX) {
                for (y in paddingY..(row / type.size.y.toInt())-1) {
                    if(checkFree(x*type.size.x.toInt(),y*type.size.y.toInt(),type.size)) {
                        pos.add(Vector2(x.toFloat()*type.size.x,y.toFloat()*type.size.y))
                    }
                }
            }
            return pos
        }
        private fun checkFree(x: Int, y: Int, size: Vector2): Boolean {
            if(!cellsFlags[x][y]) {
                return false
            }
            for (xA in 0..size.x.toInt()-1) {
                for (yA in 0..size.y.toInt()-1) {
                    if(!cellsFlags[x+xA][y+yA])
                        return false
                }
            }
            return true
        }

        fun getCells(type: Cell.Type) : Array<Cell> {
            val array = Array<Cell>()

            for(i in 0..cells.size-1) {
                if(cells[i].cellComponent.type == type)
                    array.add(cells[i])
            }

            return array
        }

    }


    class Cell(defType: Type) : Entity() {
        private val objects: Array<Entity>
        val cellComponent: CellComponent

        init {
            objects = Array()
            cellComponent = CellComponent(defType)
            add(cellComponent)
            add(CHealth(defType.startHealth))
        }

        class CellComponent(val type: Type) : Component {
            companion object : ComponentResolver<CellComponent>(CellComponent::class.java)

        }

        enum class Type(var startHealth: Float, val size: Vector2) {
            T4X4(Balance.FieldHpV3.toFloat(), Vector2(4f, 4f)),
            T2X2(Balance.FieldHpV2.toFloat(), Vector2(2f,2f)),
            T1X1(Balance.FieldHpV1.toFloat(), Vector2(1f,1f)),
            TYPE_5(15f, Vector2(5f,3f))
        }
    }
}


