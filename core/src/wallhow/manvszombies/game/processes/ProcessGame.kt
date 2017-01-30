package wallhow.manvszombies.game.processes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.Viewport
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.width
import wallhow.acentauri.process.IProcess
import wallhow.acentauri.process.ProcessManager
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.GameTable
import wallhow.manvszombies.game.Level
import wallhow.manvszombies.game.components.CGun
import wallhow.manvszombies.game.objects.GunType
import wallhow.manvszombies.game.objects.Player
import wallhow.manvszombies.game.objects.TypeZombie
import wallhow.manvszombies.game.objects.Zombie
import wallhow.manvszombies.game.setAllColor

/**
 * Created by wallhow on 13.01.17.
 */
class ProcessGame() : IProcess, InputAdapter() {
    override val name: String = "game"
    private lateinit var level : Level
    private lateinit var table : GameTable
    private lateinit var gui: Stage
    private lateinit var guiTable: Table
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
            createGUI()
            Game.engine.addEntity(player)
            input.addProcessor(gui)
            input.addProcessor(this)
        }
    }

    private fun createGUI() {
        guiTable = Table()
        guiTable.x = 0f
        guiTable.y = 0f
        guiTable.width = Game.viewport.worldWidth
        guiTable.height = Game.viewport.worldHeight
        //Game.textButtonStyle.setAllColor(Color.RED)



        val greenButton = TextButton("G",genStyle(Color.GREEN.cpy()))
        val blueButton = TextButton("B",genStyle(Color.BLUE.cpy()))
        val redButton = TextButton("R",genStyle(Color.RED.cpy()))

        greenButton.addListener {
            blueButton.isChecked=false
            redButton.isChecked = false
            CGun[player].gunType = GunType.GREEN
            true
        }
        blueButton.addListener {
            greenButton.isChecked = false
            redButton.isChecked = false
            CGun[player].gunType = GunType.BLUE
            true
        }
        redButton.addListener {
            greenButton.isChecked = false
            blueButton.isChecked = false
            CGun[player].gunType = GunType.RED
            true
        }

        //guiTable.debug = true
        guiTable.right().bottom().add(greenButton).pad(10f).row()
        guiTable.right().bottom().add(blueButton).pad(10f).row()
        guiTable.right().bottom().add(redButton).pad(10f).row()

        gui.addActor(guiTable)
    }

    private fun genStyle(color: Color) : TextButton.TextButtonStyle {
        val pixel = Pixmap(36,36, Pixmap.Format.RGBA8888)
        val c = color
        c.a = 0.5f
        pixel.setColor(c)
        pixel.fill()
        val up = TextureRegion(Texture(pixel))
        c.a = 0.75f
        pixel.setColor(c)
        pixel.fill()
        val down = TextureRegion(Texture(pixel))
        val checked = TextureRegion(Texture(pixel))
        return TextButton.TextButtonStyle(TextureRegionDrawable(up),
                TextureRegionDrawable(checked), TextureRegionDrawable(down),Game.ttfFont.get(12))

    }

    override fun load() {
        createZombie(TypeZombie.GREEN,level.mobsCountGreen,GameTable.Cell.CellType.TYPE_2)
        createZombie(TypeZombie.BLUE,level.mobsCountBlue,GameTable.Cell.CellType.TYPE_3)
        createZombie(TypeZombie.RED,level.mobsCountRed,GameTable.Cell.CellType.TYPE_1)

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
            showNewWave()
        }
    }

    private fun showNewWave() {
        val newWave = Label("Next Wave!", Label.LabelStyle(Game.ttfFont.get(40), Color.RED))
        newWave.setPosition(Gdx.graphics.width/2f,Gdx.graphics.height/2f)
        newWave.addAction(Actions.sequence(Actions.run {
            Game.level.levelUp()

        },Actions.delay(2.5f),Actions.run {
            load()
            newWave.clearActions()
            newWave.remove()
        }))

        gui.addActor(newWave)
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