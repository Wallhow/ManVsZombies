package wallhow.manvszombies.game.processes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.Viewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import ktx.vis.table
import wallhow.acentauri.process.ProcessManager
import wallhow.acentauri.process.IProcess
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.GameRecords

/**
 * Created by wallhow on 09.01.17.
 */
class ProcessMenu : IProcess {
    override val name: String
        get() = "menu"
    private val view = Game.injector.getInstance(Viewport::class.java)
    private lateinit var menuStage: Stage
    lateinit var socialGameRecords: Array<GameRecords.Record>

    override fun initialize(userInfo: Any) {
        println("process menu")
        menuStage = Stage(view, Game.injector.getInstance(SpriteBatch::class.java))

        menuStage.addActor(createVisUI())
        if((userInfo as String) == "default") {

        }
        else if(userInfo == "game") {

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
                textButton(" START ").addCaptureListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        onButton(Button.START)
                    }
                })
                row()
                textButton(" achievements ").addListener {
                    onButton(Button.ACHIEVEMENTS)
                    true
                }
                row()
                textButton(" OPTIONS ").addListener {
                    onButton(Button.OPTIONS)
                    true
                }
                row()
                textButton(" Login in ").addListener {
                    onButton(Button.LOGIN)
                    true
                }
                row()
                textButton(" EXIT ").addCaptureListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        onButton(Button.EXIT)
                    }
                })
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
                    label("local") {
                        color = Color.CYAN.cpy()
                        color.a = 0.5f
                    }
                    list<VisLabel> {
                        val records = Game.injector.getInstance(GameRecords::class.java)
                        records.get().forEach {
                            label("${it.namePlayer} wave ${it.wave}")
                        }
                    }
                }
            }

        }.apply { setPosition(view.worldWidth/2,view.worldHeight/2) }
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
        menuStage.dispose()
    }

    override fun getInputProcessListener(): InputProcessor {
        return menuStage
    }

    private fun onButton(type: Button) {
        when(type) {
            Button.START-> {Game.processManager.setCurrentProcess("game")}
            Button.ACHIEVEMENTS -> {}
            Button.OPTIONS -> {}
            Button.LOGIN -> {}
            Button.EXIT -> {Gdx.app.exit()}
        }
    }
    private enum class Button {
        START,ACHIEVEMENTS,OPTIONS,LOGIN,EXIT
    }
}