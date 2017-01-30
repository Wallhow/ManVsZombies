package wallhow.manvszombies.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
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
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton
import wallhow.acentauri.process.ProcessManager
import wallhow.acentauri.utils.TTFFont
import wallhow.acentauri.ashley.systems.*
import wallhow.manvszombies.game.systems.*
import wallhow.vkdesktop.VKGameServiceDesktop

class GameModule(game: Game) : Module {
    private var game: Game
    init {
        this.game=game
    }
    override fun configure(binder: Binder) {
        binder.bind(SpriteBatch::class.java).to(game.spriteBatch)
        binder.bind(OrthographicCamera::class.java).to(viewport().camera)
    }

    @Provides @Singleton
    fun game() : Game {
        return game
    }

    @Provides @Singleton
    fun processManager() : ProcessManager {
        return ProcessManager()
    }
    @Provides @Singleton
    fun viewport() : Viewport {
        return FillViewport(400f,600f)
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
    fun level() : Level {
        return Level()
    }

    @Provides @Singleton
    fun systems ( ) : Systems {
        return Systems(listOf(
                PlayerControllerSystem::class.java,
                //CollideDetectedSystem::class.java,
                MovementSystem::class.java,
                DrawDebugSystem::class.java,
                DrawImageSystem::class.java,
                DrawHealthSystem::class.java,
                TimerSystem::class.java,
                KickSystem::class.java,
                DeleteMeSystem::class.java,
                GunSystem::class.java,
                KickMobSystem::class.java,
                ActionsSystem::class.java,
                ShakeCellSystem::class.java
                //InvisibleSystem::class.java
        ))
    }
    @Provides @Singleton
    fun imageButtonStyle() : TextButton.TextButtonStyle {
        val pixel = Pixmap(36,36, Pixmap.Format.RGBA8888)
        val c = Color.FOREST
        c.a = 0.6f
        pixel.setColor(c)
        pixel.fill()
        val up =TextureRegion(Texture(pixel))
        val down = TextureRegion(Texture(pixel))
        val checked = TextureRegion(Texture(pixel))
        val style = TextButton.TextButtonStyle(TextureRegionDrawable(up),
                TextureRegionDrawable(checked), TextureRegionDrawable(down),ttfFont().get(12))

        return style
    }

}
data class Systems(val list: List<Class<out EntitySystem>>)