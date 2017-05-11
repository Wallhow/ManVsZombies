package wallhow.manvszombies.game.states

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Timer
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import ktx.actors.onClick
import ktx.scene2d.dialog
import ktx.vis.KVisWindow
import ktx.vis.table
import ktx.vis.window
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.width
import wallhow.acentauri.extension.actors.addActors
import wallhow.acentauri.state.State
import wallhow.acentauri.state.StateManager
import wallhow.acentauri.utils.gdxSchedule
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CHealth
import wallhow.manvszombies.game.objects.models.gun.CGun
import wallhow.manvszombies.game.objects.*
import wallhow.manvszombies.game.objects.models.gun.GunType
import wallhow.manvszombies.game.objects.models.Player
import wallhow.manvszombies.game.objects.models.TypeZombie
import wallhow.manvszombies.game.objects.models.Zombie

/**
 * Created by wallhow on 13.01.17.
 */
class StateGame() : State, InputAdapter() {
    override val name: String = "game"
    private lateinit var gameField : GameField
    private lateinit var gui: Stage
    private lateinit var input: InputMultiplexer
    private lateinit var player : Player
    private val gs = GState()
    private var gameOver = false

    private lateinit var bulletCount : VisLabel
    private lateinit var currentWave : VisLabel

    override fun initialize() {
        println("state game ")
        player = Player(Vector2(Game.viewport.worldWidth/2f,0f))
        input = InputMultiplexer()

        gui = Stage(Game.viewport,Game.injector.getInstance(SpriteBatch::class.java))
        input.addProcessor(gui)
        input.addProcessor(this)
        GameState.init()
        gameField = Game.gameField
        gs.setCurrent(SType.PLAY)
    }

    override fun load(context : State) {
        println(context)
        if(context.name == "menu") {
            initGame()
        }
        gui.addActor(createVisUI())
    }

    fun initGame() {
        println("initGame()")
        GameState.init() // Инициализируем первую волну

        createCellsInGameField()
        fillField()
        Game.engine.addEntity(player)
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

    private lateinit var upgradePoint: VisLabel
    private fun createVisUI() : VisTable {
        return ktx.vis.table {
            setPosition(0f, 0f)
            setSize(Game.viewport.worldWidth, Game.viewport.worldHeight)

            currentWave = label("wave:${GameState.level}").apply { left().top().padLeft(20f).expandX() }.actor
            upgradePoint = label("${GameState.points} points").apply { right().top().padRight(20f).row() }.actor


            //Кнопки способностей
            verticalGroup {
                textButton(" + ") {
                    padLeft(20f)
                    this.label.onClick { e,it ->
                        println("+ hp")
                        repair()
                    }
                }.left()
            }.bottom().left()
            // Кнопки оружия
            verticalGroup {
                space(8f)
                textButton("G") {
                    color = Color.GREEN.cpy()
                    color.a = 0.7f
                    padRight(40f)
                    addListener { e ->
                        color.mul(2f)
                        player.takeGreenGun()
                        true
                    }
                }
                textButton("B") {
                    color = Color.BLUE.cpy()
                    color.a = 0.7f
                    padRight(40f)
                    addCaptureListener {
                        player.takeBlueGun()
                        true
                    }
                }.right()
                textButton("R") {
                    color = Color.RED.cpy()
                    color.a = 0.8f
                    padRight(40f)
                    addListener {
                        player.takeRedGun()
                        true
                    }
                }
            }.right().bottom().expandY()
            row()

            bulletCount = label("bullet: ${player.getMaxCountBullet()}/${player.getCountBullet()}")
                    .left().padLeft(20f).actor
        }
    }

    private var isRepair: Boolean = false
    private val buttonsRepair = Array<VisTextButton>()
    private fun repair() {
        fun repairButtonsAdd() {
            //TODO Заменить русское слово Цена :D
            fun addButtonPlus(it: Entity, hpAdd : Int, цена : Int) {
                buttonsRepair.add(VisTextButton("+ $hpAdd HP").apply {
                    setPosition(CPosition[it].position.x,CPosition[it].position.y)
                    setSize(CImage[it].width,CImage[it].height)
                    label.setFontScale(0.8f)
                    color = Color.SKY
                    color.a = 0.5f

                    onClick { inputEvent, visTextButton ->
                        if(GameState.points >= цена) {
                            CHealth[it].currentHealth+=hpAdd
                            if(CHealth[it].currentHealth > CHealth[it].maxHealth) {
                                CHealth[it].maxHealth = CHealth[it].currentHealth
                            }
                            GameState.points-=цена
                        }
                        else {
                            color = Color.RED
                            color.a = 0.7f
                            gdxSchedule(0.2f) {
                                color = Color.SKY
                                color.a = 0.5f
                            }
                        }
                    }
                })
            }

            gameField.getCells(Cell.Type.T4X4).forEach {
                addButtonPlus(it,50,300)
            }
            gameField.getCells(Cell.Type.T2X2).forEach {
                addButtonPlus(it,50,200)
            }
            gameField.getCells(Cell.Type.T1X1).forEach {
                addButtonPlus(it,10,90)
            }

            gui.addActors(buttonsRepair)
        }
        fun repairButtonsRemove() {
            buttonsRepair.forEach {it.remove()}
            buttonsRepair.clear()
        }

        if (!isRepair) {
            isRepair = true
            repairButtonsAdd()
        } else {
            isRepair = false
            repairButtonsRemove()
        }
    }

    private fun createGameOverTable(): Actor {
        return ktx.vis.table {
            setPosition(0f, 0f)
            setSize(Game.viewport.worldWidth, Game.viewport.worldHeight)

            label("Game Over") {
                this.setFontScale(2.5f)
                this.setColor(0.5f, 0f, 0f, 1f)
            }.center().row()

            verticalGroup {
                space(10f)
                textButton("replay").addListener { e ->
                    gameOver = false
                    flushRecord()
                    GameState.reset()
                    initGame()
                    false
                }
                row()
                textButton("main menu").addCaptureListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        flushRecord()
                        GameState.reset()
                        gameOver = false
                        Game.stateManager.setCurrentProcess("menu")
                    }
                })
                row()
                row()

                val name = label("you " + GameState.nickName)
                name.addCaptureListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        Gdx.input.getTextInput(object : Input.TextInputListener {
                            override fun input(text: String) {
                                GameState.nickName = text
                                name.setText("you $text")
                            }

                            override fun canceled() {
                            }
                        }, "your name", GameState.nickName, "____")
                    }
                })
                row()
                label("your wave ${GameState.level}") {
                    this.setFontScale(0.9f)
                }
                row()
                val records = Game.injector.getInstance(GameRecords::class.java)
                if (records.get()[0].wave < GameState.level) {
                    label("New RECORD!") {
                        this.color = Color.RED.cpy()
                        this.setFontScale(2f)
                    }
                    row()
                }
            }
        }
    }


    private fun fillField() {
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

    override fun update(delta: Float) {
        gui.act(delta)
        bulletCount.setText("bullet: ${player.getMaxCountBullet()}/${player.getCountBullet()}")
        currentWave.setText("wave:${ GameState.level }")
        upgradePoint.setText("${GameState.points} points")

        play()

        when (gs.getCurrent()) {
            SType.PLAY -> {}
            StateGame.SType.GAME_OVER -> { }
            StateGame.SType.PAUSE -> TODO()
        }
    }
    private fun play() {
        fun gameOver() {
            gameOver = true
            Game.engine.removeAllEntities()

            val tableGameOver = createGameOverTable()
            gui.clear()
            gui.addActor(tableGameOver)

        }
        fun showNewWave() {
            GameState.levelUp()
            var ready: VisLabel? = null
            val tableNewWave = table {
                ready = label("ready?") {
                    color = Color.YELLOW
                    setFontScale(2f)
                    addAction(Actions.sequence(Actions.scaleTo(3f, 3f, 2.5f), Actions.alpha(0.0f, 2.5f)))
                }.center().actor
                setPosition(Game.viewport.worldWidth / 2 ,Game.viewport.worldHeight / 2 )
            }
            gui.addActor(tableNewWave)

            gdxSchedule(2.5f) {
                tableNewWave.removeActor(ready)
                tableNewWave.add("Tap me!").center().actor.apply {
                    color = Color.YELLOW
                    setFontScale(2f)
                    addCaptureListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            fillField()
                            event!!.target.remove()
                            tableNewWave.remove()
                        }
                    })
                }
            }

        }

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

    private fun flushRecord() {
        val records = Game.injector.getInstance(GameRecords::class.java)
        for (i in 0..records.get().size-1) {
            if(records.get()[i].wave<=GameState.level) {
                if(records.get()[i].wave==GameState.level) {
                    break
                }
                records.get()[i].namePlayer = GameState.nickName
                records.get()[i].wave = GameState.level
                records.flush()
                Game.dreamloSDK.leaderboard.setScore(GameState.nickName,GameState.level)
                break
            }
        }
    }

    override fun event(event: StateManager.Event) {
        if(event == StateManager.Event.Resize) {
        }
    }
    override fun dispose() {
        gui.dispose()
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


    class GState {
        private var preState: SType
        private var state: SType

        init {
            preState = SType.PLAY
            state = SType.PLAY
        }

        fun getCurrent() : SType = state
        fun setCurrent(state: SType) {preState = this.state; this.state = state}
        fun getPre() : SType = preState
    }
    enum class SType {
        PLAY,GAME_OVER,
        PAUSE
    }

}
