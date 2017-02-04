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
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import ktx.style.defaultStyle
import ktx.style.get
import ktx.vis.table
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
        val pixel = Pixmap(24, 24, Pixmap.Format.RGBA8888)
        pixel.setColor(Color.RED)
        pixel.fill()
        texturePixel = Texture(pixel)

        menuStage.addActor(createVisUI())
        bgRect = createBackground(60)

        if((userInfo as String) == "default") {

        }
     }

    private fun createVisUI() : Table {
        VisUI.getSkin()[VisTextButton.VisTextButtonStyle::class.java].font = Game.ttfFont.get(24)
        VisUI.getSkin()[Label.LabelStyle::class.java].font = Game.ttfFont.get(24)
        return table {
            label("Man vs Zombies") {
                this.color = Color.RED.cpy()
                color.r-=0.4f
                setFontScale(1.7f)
            }.colspan(2).padBottom(30f).row()

            //Кнопки меню
            verticalGroup {
                fill()
                textButton(" START ").addListener {
                    onButton("start")
                    true
                }
                row()
                textButton(" achievements ").addListener {
                    onButton("achievements")
                    true
                }
                row()
                textButton(" OPTIONS ").addListener {
                    onButton("options")
                    true
                }
                row()
                textButton(" Login in ").addListener {
                    onButton("loginIn")
                    true
                }
                row()

            }.padBottom(15f)
            row()
            label("leaderBoard") {
                this.setFontScale(0.7f)
                color = Color.CYAN.cpy()
                color.a = 0.9f
            }
            padBottom(20f)
            row()
            horizontalGroup {
                verticalGroup {
                    label("global") {
                        color = Color.CYAN.cpy()
                        color.a = 0.5f
                    }
                    list<VisLabel> {
                        label("record 1")
                        label("record 2")
                        label("record 3")
                        label("record 4")
                    }
                }.padRight(20f).padLeft(20f)
                separator { fill() }
                verticalGroup {
                    label("social"){
                        color = Color.CYAN.cpy()
                        color.a = 0.5f
                    }
                    list<VisLabel> {
                        label("record 1")
                        label("record 2")
                        label("record 3")
                        label("record 4")
                    }
                }.padLeft(20f).padRight(20f)
            }
            setPosition(view.worldWidth/2,view.worldHeight/2)
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

    private fun onButton(nameButton: String) {
        when(nameButton) {
            "start" -> {Game.processManager.setCurrentProcess("game")}
            "achievements" -> {}
            "options" -> {}
            "loginIn" -> {}
        }
    }
}