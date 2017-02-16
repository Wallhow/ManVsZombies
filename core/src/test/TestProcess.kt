package test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.process.ProcessManager
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.Cell
import wallhow.manvszombies.game.objects.GameField

/**
 * Created by wallhow on 15.02.17.
 */
class TestProcess() : ProcessManager.ProcessAdapter("test") {
    private lateinit var gameField : GameField
    private lateinit var input : InputAdapter

    override fun initialize(userInfo: Any) {
        super.initialize(userInfo)

        //
        val cellTexture = TextureRegion(Texture(Gdx.files.internal("assets/cells16x16.png")))
        gameField = GameField(400f,600f,40f)

        gameField.addCell(Cell.Type.T4X4,2) {
            val cell_type = Cell.Type.T4X4
            val cell = Cell(cell_type)
            val size = cell_type.size
            cell.add(CImage(cellTexture,40f*size.x,40f*size.y,16f,16f).apply {
                frameSequence= intArrayOf(MathUtils.random(0,3)) })
            Game.engine.addEntity(cell)
            cell // return
        }

        gameField.addCell(Cell.Type.T2X2,5) {
            val cell_type = Cell.Type.T2X2
            val cell = Cell(cell_type)
            val size = cell_type.size
            cell.add(CImage(cellTexture,40f*size.x,40f*size.y,16f,16f).apply {
                frameSequence= intArrayOf(MathUtils.random(0,3)) })
            Game.engine.addEntity(cell)
            cell // return
        }
        gameField.addCell(Cell.Type.T1X1,4) {
            val cell_type = Cell.Type.T1X1
            val cell = Cell(cell_type)
            val size = cell_type.size
            cell.add(CImage(cellTexture,40f*size.x,40f*size.y,16f,16f).apply {
                frameSequence= intArrayOf(MathUtils.random(0,3)) })
            Game.engine.addEntity(cell)
            cell // return
        }
        var keyPress = false


        input = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                println("press")
                if(keycode == Input.Keys.SPACE && !keyPress) {
                    println("press space")
                    keyPress = true
                    Game.engine.removeAllEntities()
                    gameField = GameField(400f,600f,40f)

                    gameField.addCell(Cell.Type.T4X4,2) {
                        val cell_type = Cell.Type.T4X4
                        val cell = Cell(cell_type)
                        val size = cell_type.size
                        cell.add(CImage(cellTexture,40f*size.x,40f*size.y,16f,16f).apply {
                            frameSequence= intArrayOf(MathUtils.random(0,3)) })
                        Game.engine.addEntity(cell)
                        cell // return
                    }

                    gameField.addCell(Cell.Type.T2X2,5) {
                        val cell_type = Cell.Type.T2X2
                        val cell = Cell(cell_type)
                        val size = cell_type.size
                        cell.add(CImage(cellTexture,40f*size.x,40f*size.y,16f,16f).apply {
                            frameSequence= intArrayOf(MathUtils.random(0,3)) })
                        Game.engine.addEntity(cell)
                        cell // return
                    }
                    gameField.addCell(Cell.Type.T1X1,4) {
                        val cell_type = Cell.Type.T1X1
                        val cell = Cell(cell_type)
                        val size = cell_type.size
                        cell.add(CImage(cellTexture,40f*size.x,40f*size.y,16f,16f).apply {
                            frameSequence= intArrayOf(MathUtils.random(0,3)) })
                        Game.engine.addEntity(cell)
                        cell // return
                    }
                }
                return false
            }

            override fun keyUp(keycode: Int): Boolean {
                if(keycode == Input.Keys.SPACE) {
                    keyPress = false
                }
                return super.keyUp(keycode)
            }
        }
        //
    }

    override fun getInputProcessListener(): InputProcessor {
        return input
    }
}