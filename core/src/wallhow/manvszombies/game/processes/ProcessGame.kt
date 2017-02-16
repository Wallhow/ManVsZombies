package wallhow.manvszombies.game.processes

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Timer
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import wallhow.acentauri.ashley.components.CImage
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
    private lateinit var gameField : GameField
    private lateinit var gui: Stage
    private lateinit var input: InputMultiplexer
    private val player = Player()
    private var gameOver = false

    private lateinit var bulletCount : VisLabel
    private lateinit var currentWave : VisLabel
    private lateinit var newWaveLabel : VisLabel



    override fun initialize(userInfo: Any) {
        if((userInfo as String) == "menu" || (userInfo) == "default") {
            input = InputMultiplexer()

            gui = Stage(Game.viewport,Game.injector.getInstance(SpriteBatch::class.java))
            gui.addActor(createVisUI())
            gui.actionsRequestRendering=true

            input.addProcessor(gui)
            input.addProcessor(this)

            initGame()
        }
    }

    private fun createCellsInGameField() {
        val cellTexture = TextureRegion(Texture(Gdx.files.internal("assets/cells16x16.png")))
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
        }.apply {  }
    }

    fun initGame() {
        GameState.init() // Инициализируем первую волну

        gameField = Game.gameField
        createCellsInGameField()
        newWaveLabel = VisLabel("New Wave", Color.YELLOW)
        Game.engine.addEntity(player)
    }

    override fun load() {
        if(gameField.getCells(Cell.Type.T4X4).size>0)
            createZombie(TypeZombie.RED,GameState.botsRed, Cell.Type.T4X4)
        else if(gameField.getCells(Cell.Type.T2X2).size>0)
            createZombie(TypeZombie.RED,GameState.botsRed, Cell.Type.T2X2)
        else
            createZombie(TypeZombie.RED,GameState.botsRed, Cell.Type.T1X1)

        if(gameField.getCells(Cell.Type.T2X2).size > 0)
            createZombie(TypeZombie.GREEN,GameState.botsGreen, Cell.Type.T2X2)
        else if(gameField.getCells(Cell.Type.T4X4).size >0 )
            createZombie(TypeZombie.GREEN,GameState.botsGreen, Cell.Type.T4X4)
        else
            createZombie(TypeZombie.GREEN,GameState.botsGreen, Cell.Type.T1X1)

        if(gameField.getCells(Cell.Type.T1X1).size > 0)
            createZombie(TypeZombie.BLUE,GameState.botsBlue, Cell.Type.T1X1)
        else if(gameField.getCells(Cell.Type.T4X4).size > 0)
            createZombie(TypeZombie.BLUE,GameState.botsBlue, Cell.Type.T4X4)
        else if(gameField.getCells(Cell.Type.T2X2).size > 0)
            createZombie(TypeZombie.BLUE,GameState.botsBlue, Cell.Type.T4X4)


    }

    private fun createZombieInEngine(type: TypeZombie, i : Int, cell: Cell) {
        val z = Zombie(type,cell)
        val r = MathUtils.random(-cell.halfSize.x,cell.halfSize.x-z.width)
        CPosition[z].position.set(CPosition[cell].position.cpy().add(cell.halfSize.x + r,cell.halfSize.y+i*3f))
        CPosition[z].zIndex = -CPosition[z].position.y.toInt()
        Game.engine.addEntity(z)
    }
    private fun createZombie(type: TypeZombie, count: Int, cellType: Cell.Type) {
        val arr = gameField.getCells(cellType)
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

    //TODO Добавить локальные рекорды

    override fun update(delta: Float) {
        gui.act(delta)
        bulletCount.setText("bullet: ${player.getMaxCountBullet()}/${player.getCountBullet()}")
        currentWave.setText("wave:${ GameState.level }")


        if(!gameOver && gameField.getCells(Cell.Type.T2X2).size == 0
        && gameField.getCells(Cell.Type.T1X1).size == 0
        && gameField.getCells(Cell.Type.T4X4).size == 0) {
            gameOver()
        }

        if(!gameOver) {
            if (GameState.newWave) {
                showNewWave()
            }
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

    private fun gameOver() {
        gameOver = true

        val tableGameOver = ktx.vis.table {
            setPosition(0f,0f)
            setSize(Game.viewport.worldWidth,Game.viewport.worldHeight)

            label("Game Over").center().row()

            verticalGroup {
                space(10f)
                textButton("replay").addListener { e ->
                    gameOver=false
                    GameState.reset()
                    initGame()
                    true
                }
                row()
                textButton("main menu").addListener { e ->
                    Game.engine.removeAllEntities()
                    GameState.reset()
                    gameOver=false
                    Game.processManager.setCurrentProcess("menu")
                    true
                }
            }
        }

        gui.clear()
        gui.addActor(tableGameOver)
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