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
import wallhow.manvszombies.game.Game

/**
 * Created by wallhow on 09.01.17.
 */
class ProcessMenu : IProcess {
    override val name: String
        get() = "menu"
    private val menuStage: Stage
    private val view: Viewport

    init {
        menuStage = Stage(Game.injector.getInstance(Viewport::class.java),Game.injector.getInstance(SpriteBatch::class.java))
        view = Game.injector.getInstance(Viewport::class.java)

    }

    override fun initialize(userInfo: Any) {
        menuStage.addActor(createVisUI())
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
    }

    override fun render(sb: SpriteBatch) {
        menuStage.draw()
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