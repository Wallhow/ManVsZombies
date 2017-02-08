package wallhow.manvszombies.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.Viewport
import com.google.inject.*
import com.kotcrab.vis.ui.VisUI
import wallhow.acentauri.process.ProcessManager
import wallhow.acentauri.utils.ozmod.APlayer
import wallhow.acentauri.utils.TTFFont
import wallhow.manvszombies.game.objects.models.Bot
import wallhow.manvszombies.game.objects.BotListener
import wallhow.manvszombies.game.processes.ProcessGame
import wallhow.manvszombies.game.processes.ProcessMenu

/**
 * Created by wallhow on 09.01.17.
 */
class Game : ApplicationAdapter() {
    var spriteBatch: SpriteBatch? = null
    private lateinit var pManager: ProcessManager
    private lateinit var menu : ProcessMenu
    private lateinit var game : ProcessGame

    private var bgRect = Array<Rectangle>()


    private val audioPlayer = APlayer()

    private fun createBackground(count : Int): Array<Rectangle> {
        val rects = Array<Rectangle>()
        for (i in 0..count) {
            val size = MathUtils.random(4,24).toFloat()
            val x = MathUtils.random(0f, viewport.worldWidth-size)
            val y = MathUtils.random(0f,viewport.worldHeight-size)
            val rect = Rectangle(x,y,size,size)
            rects.add(rect)
        }
        return rects
    }

    override fun create() {
        val pixel = Pixmap(24, 24, Pixmap.Format.RGBA8888)
        pixel.setColor(Color.RED)
        pixel.fill()
        texturePixel = Texture(pixel)


        VisUI.load(VisUI.SkinScale.X2) // ГУЙ

        audioPlayer.addMusic("assets/sounds/test4.xm","music")
        audioPlayer.addMusic("assets/sounds/test.mod","music1")
        //audioPlayer.play("music1")

        injector = Guice.createInjector(GameModule(this))
        deadMob.add(getBotListener())


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
        bgRect = createBackground(60)
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
        bgDraw(spriteBatch as SpriteBatch)
        bgProcess(Gdx.graphics.deltaTime)
        engine.update(Gdx.graphics.deltaTime)
        pManager.render(spriteBatch as SpriteBatch)
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
                rect.y = viewport.worldHeight + rect.height
            }
        }
    }


    override fun resume() {
        pManager.resume()
    }
    override fun dispose() {
        pManager.dispose()
        VisUI.dispose()
    }

    companion object {
        lateinit var injector: Injector
        private val deadMob = Signal<Bot>()

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
        val textButtonStyle : TextButton.TextButtonStyle
            get() = injector.getInstance(TextButton.TextButtonStyle::class.java)
        val viewport : Viewport
            get() = injector.getInstance(Viewport::class.java)

        fun getDeadMobSignaler() : Signal<Bot> {
            return deadMob
        }
        fun getBotListener() = injector.getInstance(BotListener::class.java)

        lateinit var texturePixel : Texture

        var pause = false
    }
}