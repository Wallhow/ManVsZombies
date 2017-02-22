package wallhow.acentauri

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture

/**
 * Created by wallhow on 21.02.17.
 */
open class CoreGame : ApplicationAdapter() {
    companion object {
        lateinit var pixel : Texture
    }

    override fun create() {
        super.create()
        pixel = Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(Color.WHITE)
            fill()
        }.let(::Texture)
    }

    fun clearScreen(color : Float) {
        Gdx.gl.glClearColor(color, color, color, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun dispose() {
        super.dispose()
        pixel.dispose()
    }
}