package wallhow.manvszombies.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.*
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton
import wallhow.acentauri.state.StateManager
import wallhow.acentauri.utils.TTFFont
import wallhow.acentauri.ashley.systems.*
import wallhow.acentauri.utils.social.GameService
import wallhow.manvszombies.game.objects.models.Bot
import wallhow.manvszombies.game.objects.BotListener
import wallhow.manvszombies.game.objects.CellListener
import wallhow.manvszombies.game.objects.GameRecords
import wallhow.manvszombies.game.objects.models.gun.GunSystem
import wallhow.manvszombies.game.states.StateGame
import wallhow.manvszombies.game.systems.*

class GameModule(game: Game) : Module {
    private var game: Game
    init {
        this.game=game
    }
    override fun configure(binder: Binder) {
        binder.bind(SpriteBatch::class.java).to(game.spriteBatch)
        binder.bind(OrthographicCamera::class.java).to(viewport().camera)
        binder.bind(GameRecords::class.java).to(game.gameRecords)
    }

    @Provides @Singleton
    fun game() : Game {
        return game
    }

    @Provides @Singleton
    fun processManager() : StateManager {
        return StateManager()
    }
    @Provides @Singleton
    fun viewport() : Viewport {
        return FitViewport(480f,800f)
    }
    @Provides @Singleton
    fun ttfFont() : TTFFont {
        return TTFFont("assets/pixel.ttf")
    }

    @Provides @Singleton
    fun atlas() : TextureAtlas {
        return TextureAtlas(Gdx.files.internal("assets/atlas.atlas"))
    }

    @Provides @Singleton
    fun engine() : Engine {
        return Engine()
    }

    @Provides @Singleton
    fun systems ( ) : Systems {
        return Systems(listOf(
                MovementSystem::class.java,//DrawDebugSystem::class.java,
                DrawImageSystem::class.java,
                DrawSpriteSystem::class.java,
                DrawHealthSystem::class.java,
                TaskSystem::class.java,
                KickSystem::class.java,
                DeleteSystem::class.java,
                GunSystem::class.java,
                KickMobSystem::class.java,
                ActionsSystem::class.java,
                ShakeCellSystem::class.java
        ))
    }
    @Provides @Singleton
    fun botListener() : BotListener {
        return BotListener()
    }
    @Provides @Singleton
    fun cellListener() = CellListener()

}
data class Systems(val list: List<Class<out EntitySystem>>)