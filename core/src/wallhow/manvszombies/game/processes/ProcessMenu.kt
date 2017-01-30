package wallhow.manvszombies.game.processes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.Viewport
import wallhow.acentauri.process.ProcessManager
import wallhow.acentauri.process.IProcess
import wallhow.acentauri.utils.TTFFont
import wallhow.acentauri.vkgs.VKGameService
import wallhow.manvszombies.game.Game
import wallhow.vkdesktop.Profile
import wallhow.vkdesktop.VKGameServiceDesktop

/**
 * Created by wallhow on 09.01.17.
 */
class ProcessMenu : IProcess {
    override val name: String
        get() = "menu"

    private val menuStage: Stage
    private lateinit var tab: Table
    private var bgRect = Array<Rectangle>()
    private val view: Viewport
    private lateinit var texturePixel : Texture

    init {
        menuStage = Stage(Game.injector.getInstance(Viewport::class.java),Game.injector.getInstance(SpriteBatch::class.java))
        view = Game.injector.getInstance(Viewport::class.java)

    }

    private fun createBackground(count : Int): Array<Rectangle> {
        val rects = Array<Rectangle>()
        for (i in 0..count) {
            val size = MathUtils.random(4,24).toFloat()
            val x = MathUtils.random(0f,view.worldWidth-size)
            val y = MathUtils.random(0f,view.worldHeight-size)
            val rect = Rectangle(x,y,size,size)
            rects.add(rect)
        }
        return rects
    }

    override fun initialize(userInfo: Any) {
        val table = Table()
        table.x = (view.worldWidth / 2f)
        table.y = (view.worldHeight / 2f)

        val pixel = Pixmap(24,24,Pixmap.Format.RGBA8888)
        pixel.setColor(Color.RED)
        pixel.fill()
        texturePixel = Texture(pixel)
        val up = TextureRegion(Texture(pixel))

        val checked = TextureRegion(Texture(pixel))

        pixel.setColor(Color.BLUE)
        pixel.fill()
        val down = TextureRegion(Texture(pixel))
        Game.ttfFont.createFont(36, Color.BLUE).createFont(23,Color.CYAN).createFont(16,Color.OLIVE)
        val font = Game.ttfFont.get(36)
        val textButtonStyle = TextButton.TextButtonStyle(TextureRegionDrawable(up),
                TextureRegionDrawable(checked),TextureRegionDrawable(down),font)
        textButtonStyle.downFontColor = Color.RED
        textButtonStyle.checkedFontColor = Color.RED
        textButtonStyle.disabledFontColor = Color.RED
        textButtonStyle.fontColor = Color.RED
        textButtonStyle.overFontColor = Color.RED
        textButtonStyle.pressedOffsetY = -2f

        val buttonStart = TextButton(" start ",textButtonStyle)
        val buttonRecords = TextButton(" records ",textButtonStyle)
        val nameGame = Label("Man vs Zombies",Label.LabelStyle().apply {
            this.font = font
        })
        buttonStart.label.setFontScale(2f,2f)
        buttonStart.addListener {
            Game.processManager.setCurrentProcess("game")
            true
        }

        //val profile = Game.injector.getInstance(Game::class.java).vkGameService.getMyProfile()
        //val nameUser = Label("${profile.firstName} ${profile.lastName} level ${profile.level}",Label.LabelStyle().apply {
        //    this.font =  Game.ttfFont.get(16)
        //})

        //val rec2 = LabelList()
        //Game.injector.getInstance(Game::class.java).vkGameService.getRecordsUser().forEach {
        //    rec2.labelList.add(Label("${it.firstName} ${it.lastName} " +
         //           "level ${Game.injector.getInstance(Game::class.java).vkGameService.getLevel(it.id)}",Label.LabelStyle().apply {
         //       this.font =  Game.ttfFont.get(23)
         //   }))
        //}



        val rec2 = LabelList("1 record", "2 record", "3 record", "4 record")

        //table.left().bottom().add(nameUser).row()
        table.center()
        table.add(nameGame).row()
        table.row().spaceTop(90f)
        table.add(buttonStart).row()
        table.add(buttonRecords).row()
        rec2.labelList.forEach {
            table.add(it).row()
        }
        //table.center().add(horizontalGroup)
        tab= table
        menuStage.addActor(table)
        println("${tab.x} + ${tab.y}")

        bgRect = createBackground(60)

        if((userInfo as String) == "default") {

        }
     }

    override fun load() {

    }

    override fun update(delta: Float) {
        menuStage.act()
        bgProcess(delta)
    }

    override fun render(sb: SpriteBatch) {
        bgDraw(sb)
        menuStage.draw()
    }

    private fun bgDraw(sb: SpriteBatch) {
        sb.begin()
        sb.color = Color.DARK_GRAY
        for ( rect in bgRect) {
            sb.draw(texturePixel,rect.x,rect.y,rect.width,rect.height)
        }
        sb.color = Color.WHITE
        sb.end()
    }

    private val  speedMove = 50

    private fun bgProcess(delta: Float) {
        for ( rect in bgRect) {
            rect.y -=speedMove*delta
            if(rect.y < -rect.height) {
                rect.y = view.worldHeight + rect.height
            }
        }
    }

    override fun event(event: ProcessManager.EventProcess) {
    }

    override fun dispose() {
    }

    override fun getInputProcessListener(): InputProcessor {
        return menuStage
    }


    class ListRecords() {
        val records = Array<String>()

        init {
            for ( i in 0..3) records.add("$i testRecord")
        }
    }

    class LabelList(vararg name: String) {
        val labelList = Array<Label>()

        init {
            val step = 1f/ name.size

            for ( i in 0..name.size - 1 ) {
                labelList.add(Label(name[i],Label.LabelStyle().apply {
                    this.font = Game.ttfFont.get(23)
                    this.fontColor = Color.WHITE.cpy()
                    this.fontColor.a = 1f-step*i
                }))
            }
        }

    }
}