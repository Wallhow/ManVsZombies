package wallhow.manvszombies.game.processes

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.kotcrab.vis.ui.widget.VisTable
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.width
import wallhow.acentauri.process.IProcess
import wallhow.acentauri.process.ProcessManager
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.GameTable
import wallhow.manvszombies.game.Level
import wallhow.manvszombies.game.components.CGun
import wallhow.manvszombies.game.objects.GunType
import wallhow.manvszombies.game.objects.Player
import wallhow.manvszombies.game.objects.TypeZombie
import wallhow.manvszombies.game.objects.Zombie

/**
 * Created by wallhow on 13.01.17.
 */
class ProcessGame() : IProcess, InputAdapter() {
    override val name: String = "game"
    private lateinit var level : Level
    private lateinit var table : GameTable
    private lateinit var gui: Stage
    private lateinit var input: InputMultiplexer
    private val player = Player()
    override fun initialize(userInfo: Any) {
        level = Game.level
        if((userInfo as String) == "menu" || (userInfo) == "default") {
            input = InputMultiplexer()
            level.init()
            table = GameTable(400f,600f)

            gui = Stage(Game.viewport,Game.injector.getInstance(SpriteBatch::class.java))
            println("${gui.viewport.screenX} + ${gui.viewport.screenY} + ${gui.viewport.worldWidth} + ${gui.viewport.worldHeight}")
            //createGUI()
            gui.addActor(createVisUI())
            Game.engine.addEntity(player)
            input.addProcessor(gui)
            input.addProcessor(this)
        }
    }

    private fun createVisUI() : VisTable {
        return ktx.vis.table {
            setPosition(0f,0f)
            setSize(Game.viewport.worldWidth,Game.viewport.worldHeight)

            label("wave:${Game.level.levelNumber}").left().expandX().row()
            verticalGroup {
                fill()
                val g = textButton("G") {
                    color = Color.GREEN.cpy()
                    color.a=0.7f
                }
                g.padRight(30f)
                row()
                space(10f)

                val b = textButton("B"){
                    color = Color.BLUE.cpy()
                    color.a=0.7f

                }
                b.padRight(30f)
                val r = textButton("R"){
                    color = Color.RED.cpy()
                    color.a=0.7f

                }
                r.padRight(30f).width = 100f

                g.addListener {e ->
                    if(e.isHandled) {
                        CGun[player].gunType = GunType.GREEN
                    }
                    true
                }
                b.addListener {
                    CGun[player].gunType = GunType.BLUE


                    true
                }
                r.addListener {
                    CGun[player].gunType = GunType.RED
                    true
                }

            }.right().bottom().expand()
            //setPosition(Game.viewport.worldWidth/2,Game.viewport.worldHeight/2)
        }.apply {  }
    }

    override fun load() {
        createZombie(TypeZombie.GREEN,level.mobsCountGreen, GameTable.Cell.CellType.TYPE_2)
        createZombie(TypeZombie.BLUE,level.mobsCountBlue, GameTable.Cell.CellType.TYPE_3)
        createZombie(TypeZombie.RED,level.mobsCountRed, GameTable.Cell.CellType.TYPE_1)

    }
    private fun createZombieInEngine(type: TypeZombie,i : Int,cell: GameTable.Cell) {
        val z = Zombie(type,cell)
        val r = MathUtils.random(-cell.halfSize.x,cell.halfSize.x-z.width)
        CPosition[z].position.set(CPosition[cell].position.cpy().add(cell.halfSize.x + r,cell.halfSize.y+i*3f))
        CPosition[z].zIndex = -CPosition[z].position.y.toInt()
        Game.engine.addEntity(z)
    }
    private fun createZombie(type: TypeZombie, count: Int,cellType: GameTable.Cell.CellType) {
        val arr = table.cells.getCells(cellType)
        var c = count
        var i = 0
        while (c!=0) {
            if(i>=arr.size)
                i=0
            createZombieInEngine(type,count - c,arr[i])
            i++
            c--
        }
    }
    override fun render(sb: SpriteBatch) {
        gui.batch.projectionMatrix=gui.camera.combined
        gui.draw()
    }

    override fun update(delta: Float) {
        gui.act(delta)

        if(Game.level.mobsCount<=0) {
            //showNewWave()
        }
    }



    override fun event(event: ProcessManager.EventProcess) {
    }

    override fun dispose() {

    }

    override fun getInputProcessListener(): InputProcessor? {
        return input
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val vec = Game.viewport.camera.unproject(Vector3(screenX.toFloat(),screenY.toFloat(),1f))
        player.see(vec.x,vec.y)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return super.touchUp(screenX, screenY, pointer, button)
    }
}