package wallhow.manvszombies.game.processes

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Timer
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.width
import wallhow.acentauri.process.IProcess
import wallhow.acentauri.process.ProcessManager
import wallhow.acentauri.utils.gdxSchedule
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.models.gun.CGun
import wallhow.manvszombies.game.objects.*
import wallhow.manvszombies.game.objects.models.gun.GunType
import wallhow.manvszombies.game.objects.models.Player
import wallhow.manvszombies.game.objects.models.TypeZombie
import wallhow.manvszombies.game.objects.models.Zombie

/**
 * Created by wallhow on 13.01.17.
 */
class ProcessGame() : IProcess, InputAdapter() {
    override val name: String = "game"
    private lateinit var table : GameTable
    private lateinit var gui: Stage
    private lateinit var input: InputMultiplexer
    private val player = Player()

    private lateinit var bulletCount : VisLabel
    private lateinit var currentWave : VisLabel
    private lateinit var newWaveLabel : VisLabel

    override fun initialize(userInfo: Any) {
        if((userInfo as String) == "menu" || (userInfo) == "default") {
            input = InputMultiplexer()
            GameState.init() // Инициализируем первую волну

            table = GameTable(400f,600f)

            gui = Stage(Game.viewport,Game.injector.getInstance(SpriteBatch::class.java))
            gui.addActor(createVisUI())
            gui.actionsRequestRendering=true

            newWaveLabel = VisLabel("New Wave", Color.YELLOW)
            Game.engine.addEntity(player)
            input.addProcessor(gui)
            input.addProcessor(this)
        }
    }

    private fun createVisUI() : VisTable {
        return ktx.vis.table {
            setPosition(0f,0f)
            setSize(Game.viewport.worldWidth,Game.viewport.worldHeight)

            currentWave = label("wave:${GameState.level}").apply { left().expandX().row() }.actor
            val g = textButton("G") {
                color = Color.GREEN.cpy()
                color.a = 0.7f
                padRight(40f)
                addListener { e ->
                    player.takeGreenGun()
                    true
                }
                debug()
            }.expandY().expandX().bottom().right().row()
            val b = textButton("B") {
                color = Color.BLUE.cpy()
                color.a = 0.7f
                padRight(40f)
                addCaptureListener {
                    player.takeBlueGun()
                    true
                }
            }.right().space(10f).row()
            val r = textButton("R") {
                color = Color.RED.cpy()
                color.a = 0.7f
                padRight(40f)
                addListener {
                    player.takeRedGun()
                    true
                }
            }.right().row()

            row().left()
            bulletCount = label("bullet: ${player.getMaxCountBullet()}/${player.getCountBullet()}").actor
            //setPosition(Game.viewport.worldWidth/2,Game.viewport.worldHeight/2)
        }.apply {  }
    }

    override fun load() {
        createZombie(TypeZombie.GREEN,GameState.botsGreen, GameTable.Cell.Type.T2X2)
        createZombie(TypeZombie.BLUE,GameState.botsBlue, GameTable.Cell.Type.T1X1)
        createZombie(TypeZombie.RED,GameState.botsRed, GameTable.Cell.Type.T4X4)

    }

    private fun createZombieInEngine(type: TypeZombie, i : Int, cell: GameTable.Cell) {
        val z = Zombie(type,cell)
        val r = MathUtils.random(-cell.halfSize.x,cell.halfSize.x-z.width)
        CPosition[z].position.set(CPosition[cell].position.cpy().add(cell.halfSize.x + r,cell.halfSize.y+i*3f))
        CPosition[z].zIndex = -CPosition[z].position.y.toInt()
        Game.engine.addEntity(z)
    }
    private fun createZombie(type: TypeZombie, count: Int, cellType: GameTable.Cell.Type) {
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
        gui.batch.projectionMatrix = gui.camera.combined
        gui.draw()
    }

    //TODO Продумать и добавить систему улучшений за получаемы очки от убийства

    //TODO Наконец уже сделать гамовер! :D

    override fun update(delta: Float) {
        gui.act(delta)
        bulletCount.setText("bullet: ${player.getMaxCountBullet()}/${player.getCountBullet()}")
        currentWave.setText("wave:${ GameState.level }")

        if(GameState.newWave) {
            showNewWave()
        }
    }

    private fun showNewWave() {
        println("new Wave")
        GameState.levelUp()
        val timeReadyLabelShow = 2.5f
        val readyLabel = VisLabel("Ready?", Color.YELLOW)
        readyLabel.scaleBy(2f)
        readyLabel.setPosition(Game.viewport.worldWidth/2-readyLabel.width/2,Game.viewport.worldHeight/2-readyLabel.height/2)
        val a = Actions.sequence(Actions.scaleTo(3f,3f,timeReadyLabelShow),Actions.alpha(0.0f,timeReadyLabelShow))
        readyLabel.addAction(a)
        gui.addActor(readyLabel)

        gdxSchedule(timeReadyLabelShow) {
            gui.actors.removeValue(readyLabel,true)
            val newWaveLabel = VisLabel("Next Wave ${GameState.level}", Color.YELLOW)
            newWaveLabel.setPosition(Game.viewport.worldWidth/2-newWaveLabel.width/2,Game.viewport.worldHeight/2-newWaveLabel.height/2)
            newWaveLabel.addAction(Actions.sequence(Actions.scaleTo(2f,2f,2f),Actions.alpha(0.0f,2f)))
            gui.addActor(newWaveLabel)

            gdxSchedule(2f) {
                newWaveLabel.remove()
                load()
            }
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