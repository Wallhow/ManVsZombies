package wallhow.manvszombies.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.Viewport
import com.google.inject.*
import com.kotcrab.vis.ui.VisUI
import wallhow.acentauri.CoreGame
import wallhow.acentauri.extension.graphics.drawGridLine
import wallhow.acentauri.state.StateManager
import wallhow.acentauri.utils.ozmod.APlayer
import wallhow.acentauri.utils.TTFFont
import wallhow.acentauri.utils.social.GameService
import wallhow.dreamlo.sdk.DreamloSDK
import wallhow.manvszombies.game.objects.*
import wallhow.manvszombies.game.objects.models.Bot
import wallhow.manvszombies.game.states.StateGame
import wallhow.manvszombies.game.states.StateMenu

/**
 * Created by wallhow on 09.01.17.
 */
class Game(gameService: GameService) : CoreGame() {
    var spriteBatch: SpriteBatch? = null
    private lateinit var pManager: StateManager
    private lateinit var menu : StateMenu
    private lateinit var game : StateGame
    lateinit var gameRecords: GameRecords


    private var bgRect = Array<Rectangle>()


    private val audioPlayer = APlayer()

    init {
        //Test
        gs = gameService
    }

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
        super.create()
        dreamloSDK = DreamloSDK("58aed71468fc111e808411c7","PQQZwh_kZkSYXwCCHL6ItwHy8bppH-z0OpesFvQPh9Yw")

        val authSocial = false//gs.authorize("7ddf459aea673ce1e4")//Gdx.app.getPreferences("service").getString("code"))

        val socialRecords = Array<GameRecords.Record>()
        if(authSocial) {
            gs.getFriends().forEach {
                val record = GameRecords.Record().apply {
                    namePlayer = "${it.firstName} ${it.lastName}"
                    //wave = it.level
                }
                socialRecords.add(record)
            }

        } else {
            for (i in 0..3) {
                val record = GameRecords.Record()
                record.namePlayer = "no connect"
                record.wave = 0

                socialRecords.add(record)
            }
        }

        gameRecords = GameRecords()
        achievemets = Achievements(Gdx.app.getPreferences("localStorageA"))

        VisUI.load(VisUI.SkinScale.X2) // ГУЙ

        audioPlayer.addMusic("assets/sounds/test4.xm","music")
        audioPlayer.addMusic("assets/sounds/test.mod","music1")
        //audioPlayer.play("music1")

        injector = Guice.createInjector(GameModule(this))
        deadMob.add(getBotListener())
        deadCell.add(getCellListener())


        spriteBatch = SpriteBatch()

        injector.getInstance(Systems::class.java).list.map { injector.getInstance(it) }.forEach { system ->
            engine.addSystem(system)
        }
        pManager = stateManager
        menu = StateMenu().apply { socialGameRecords = socialRecords }
        game = StateGame()
        Gdx.input.inputProcessor = pManager

        pManager.addProcess(menu,true)
        pManager.addProcess(game)
        bgRect = createBackground(60)
    }

    override fun render() {
        clearScreen(0f)
        spriteBatch?.projectionMatrix = viewport.camera.combined
        spriteBatch?.run {
            begin()
            drawBackground(this)
            drawGridLine(pixel, viewport.worldWidth, viewport.worldHeight,40f,5f,Color.DARK_GRAY)
            end()
        }
        updateBackground(Gdx.graphics.deltaTime)
        engine.update(Gdx.graphics.deltaTime)
        pManager.render(spriteBatch as SpriteBatch)
    }

    override fun pause() {
        pManager.pause()
    }
    override fun resize(width: Int, height: Int) {
        viewport.update(width,height,true)
        viewport.apply()
        pManager.resize(width,height)
    }

    private fun drawBackground(sb: SpriteBatch) {
        sb.color = Color.RED.cpy().sub(0.3f,0.3f,0.3f,0f)
        for ( rect in bgRect) {
            sb.draw(pixel,rect.x,rect.y,rect.width,rect.height)
        }
        sb.color = Color.WHITE
    }
    private val  speedMove = 50
    private fun updateBackground(delta: Float) {
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
        audioPlayer.chipPlayer?.done()
        dreamloSDK.leaderboard.saveInLocalStorage()
        Game.injector.getInstance(GameRecords::class.java).flush()
        super.dispose()
        pManager.dispose()
        VisUI.dispose()
        ttfFont.dispose()
        atlas.dispose()
        spriteBatch?.dispose()
    }

    companion object {
        lateinit var injector: Injector
        val gameField = GameField(480f,800f,40f)
        private val deadMob = Signal<Bot>()
        private val deadCell = Signal<Cell>()
        lateinit var gs : GameService
        lateinit var dreamloSDK : DreamloSDK

        val stateManager: StateManager
            get() = injector.getInstance(StateManager::class.java)

        val ttfFont : TTFFont
            get() = injector.getInstance(TTFFont::class.java)
        val atlas : TextureAtlas
            get() = injector.getInstance(TextureAtlas::class.java)
        val engine : Engine
            get() = injector.getInstance(Engine::class.java)
        val viewport : Viewport
            get() = injector.getInstance(Viewport::class.java)

        fun getDeadMobSignaler() : Signal<Bot> {
            return deadMob
        }
        fun getDeadCellSignaler() : Signal<Cell> {
            return deadCell
        }
        fun getBotListener() = injector.getInstance(BotListener::class.java)
        fun getCellListener() = injector.getInstance(CellListener::class.java)

        var pause = false

        fun getGameService(): GameService {
            return gs
        }

        lateinit var achievemets: Achievements
    }
}