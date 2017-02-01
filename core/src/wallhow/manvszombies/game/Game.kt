package wallhow.manvszombies.game

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.Viewport
import com.google.inject.*
import wallhow.acentauri.process.ProcessManager
import wallhow.acentauri.utils.ozmod.APlayer
import wallhow.acentauri.utils.TTFFont
import wallhow.manvszombies.game.processes.ProcessGame
import wallhow.manvszombies.game.processes.ProcessMenu
import wallhow.vkdesktop.VKGameServiceDesktop

/**
 * Created by wallhow on 09.01.17.
 */
class Game : ApplicationAdapter() {
    var spriteBatch: SpriteBatch? = null
    private lateinit var pManager: ProcessManager
    private lateinit var menu : ProcessMenu
    private lateinit var game : ProcessGame

    private val audioPlayer = APlayer()

    //val vkGameService = VKGameServiceDesktop()
    init {

    }

    override fun create() {
        audioPlayer.addMusic("assets/sounds/test4.xm","music")
        audioPlayer.addMusic("assets/sounds/test.mod","music1")
        //audioPlayer.play("music1")
        //vkGameService.auth("1e2f0109bec113eb7b")
        //vkGameService.auth("f3a104999a5fe39dda")

        injector = Guice.createInjector(GameModule(this))

        //println(vkGameService.getLevel())
        //vkGameService.setLevel(6)

        //println(vkGameService.getRecords(false)[0].toString())
        println("<-- -->\n")

        spriteBatch = SpriteBatch()

        injector.getInstance(Systems::class.java).list.map { injector.getInstance(it) }.forEach { system ->
            engine.addSystem(system)
        }
        pManager = processManager
        menu = ProcessMenu()
        game = ProcessGame()
        Gdx.input.inputProcessor = pManager

        pManager.addProcess(menu,true)
        pManager.addProcess(game)
    }

    override fun pause() {
        pManager.pause()
    }
    override fun resize(width: Int, height: Int) {
        injector.getInstance(Viewport::class.java).update(width,height,true)
        pManager.resize(width,height)
    }
    override fun render() {
        cs(0f)
        spriteBatch?.projectionMatrix = injector.getInstance(Viewport::class.java).camera.combined
        engine.update(Gdx.graphics.deltaTime)
        pManager.render(spriteBatch as SpriteBatch)
    }
    override fun resume() {
        pManager.resume()
    }
    override fun dispose() {
        pManager.dispose()
    }

    companion object {
        lateinit var injector: Injector

        fun cs(color : Float) {
            Gdx.gl.glClearColor(color, color, color, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        }
        val processManager : ProcessManager
            get() = injector.getInstance(ProcessManager::class.java)

        val ttfFont : TTFFont
            get() = injector.getInstance(TTFFont::class.java)

        val atlas : TextureAtlas
            get() = injector.getInstance(TextureAtlas::class.java)
        val engine : Engine
            get() = injector.getInstance(Engine::class.java)
        val level : Level
            get() = injector.getInstance(Level::class.java)
        val textButtonStyle : TextButton.TextButtonStyle
            get() = injector.getInstance(TextButton.TextButtonStyle::class.java)
        val viewport : Viewport
            get() = injector.getInstance(Viewport::class.java)
    }
}

fun TextButton.TextButtonStyle.setAllColor(color: Color) {
    this.downFontColor = color
    this.checkedFontColor = color
    this.disabledFontColor = color
    this.fontColor = Color.DARK_GRAY
    this.overFontColor = color
}