package wallhow.manvszombies.game.states

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
import com.kotcrab.vis.ui.widget.Separator
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import ktx.actors.onClick
import ktx.vis.KHorizontalGroup
import ktx.vis.KVerticalGroup
import ktx.vis.KVisTextButton
import ktx.vis.table
import wallhow.acentauri.state.StateManager
import wallhow.acentauri.state.State
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.Achievements
import wallhow.manvszombies.game.objects.GameRecords

/**
 * Created by wallhow on 09.01.17.
 */
class StateMenu : State {
    override val name: String
        get() = "menu"
    private val view = Game.injector.getInstance(Viewport::class.java)
    private lateinit var menuStage: Stage
    lateinit var socialGameRecords: Array<GameRecords.Record>

    override fun initialize() {
        println("state menu")
        VisUI.getSkin()[VisTextButton.VisTextButtonStyle::class.java].font = Game.ttfFont.get(24)
        VisUI.getSkin()[Label.LabelStyle::class.java].font = Game.ttfFont.get(24)
     }
    override fun load(context : State) {
        menuStage = Stage(view, Game.injector.getInstance(SpriteBatch::class.java))
        showMenu()
    }

    override fun update(delta: Float) {
        menuStage.act()
    }

    override fun render(sb: SpriteBatch) {
        menuStage.draw()
    }

    override fun event(event: StateManager.Event) {
    }

    override fun dispose() {
        menuStage.dispose()
    }

    override fun getInputProcessListener(): InputProcessor {
        return menuStage
    }

    private fun onButton(type: Button) {
        when(type) {
            Button.START-> {Game.stateManager.setCurrentProcess("game")}
            Button.ACHIEVEMENTS -> { showAchievements() }
            Button.OPTIONS -> { showOptions() }
            Button.LOGIN -> {}
            Button.EXIT -> {Gdx.app.exit()}
        }
    }

    private fun showMenu() {
        menuStage.clear()
        menuStage.addActor(table {
            center()
            label("Man vs Zombies") {
                this.color = Color.RED.cpy()
                color.r-=0.3f
                setFontScale(1.7f)
            }.padBottom(30f).row()

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
                            label("${it.namePlayer} wave ${it.wave}").apply {
                                setScale(0.8f)
                            }
                        }
                    }
                    label("global") {
                        color = Color.CYAN.cpy()
                        color.a = 0.5f
                    }
                    list<VisLabel> {
                        val records = Game.dreamloSDK.leaderboard.getTopUsers(5)!!
                        records.forEach {
                            label("${it.name} wave ${it.score}").apply {
                                setScale(0.8f)
                            }
                        }
                    }
                }
            }

        }.apply { this.width = Game.viewport.worldWidth
            this.height = Game.viewport.worldHeight
            println(Game.viewport.worldWidth)
            println(Game.viewport.worldHeight)
        })
    }

    private fun showAchievements() {
        menuStage.clear()

        val verticalGroup = VerticalGroup().apply {
            center()
            Achievements.Internal.values().forEach {
                addActor(VisLabel(it.nameAchievement,Color.WHITE).apply {
                    setFontScale(1.2f)
                })
                addActor(VisLabel(it.text,Color.WHITE).apply {
                    setFontScale(0.9f)
                })
                addActor(Separator())
            }
        }

        menuStage.addActor(table {
            this.width = Game.viewport.worldWidth
            this.height = Game.viewport.worldHeight
            center()

            scrollPane(verticalGroup).center()
                    .height(Game.viewport.worldHeight/2).expandX().fillX().row()

            textButton("back") {
                onClick { event: InputEvent, button: KVisTextButton ->
                    showMenu()
                }
            }.padTop(20f)




        })
    }

    private lateinit var vibration: VisCheckBox
    private lateinit var sound: VisCheckBox
    private lateinit var music: VisCheckBox
    private fun showOptions() {
        menuStage.clear()

        menuStage.addActor(table {
            this.width = Game.viewport.worldWidth
            this.height = Game.viewport.worldHeight
            center()
            verticalGroup {
                fill()
                vibration = checkBox("vibration")
                row()
                music = checkBox("music")
                row()
                sound = checkBox("sound")
                row()
            }
            row()

            verticalGroup {
                fill()
                textButton("append") {
                    onClick { event: InputEvent, button: KVisTextButton ->
                        val pref = Gdx.app.getPreferences("options")

                        pref.putBoolean("vibration", vibration.isChecked)
                        pref.putBoolean("sound", sound.isChecked)
                        pref.putBoolean("music", music.isChecked)
                        showMenu()
                    }
                }.row()
                textButton("back") {
                    onClick { inputEvent:
                              InputEvent, kVisTextButton: KVisTextButton ->
                        showMenu()
                    }
                }
            }.padTop(20f)
        })
    }

    private enum class Button {
        START,ACHIEVEMENTS,OPTIONS,LOGIN,EXIT
    }
}