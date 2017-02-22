package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.utils.TTFFont


/**
 * Created by wallhow on 15.12.16.
 */
class DrawDebugSystem @Inject constructor(val camera: OrthographicCamera, val batch: SpriteBatch) : IteratingSystem(Family.all(CPosition::class.java)
        .get()){
    val font : BitmapFont
    val p : Texture
    val colorGrid : Color
    init {
        font = TTFFont("assets/pixel.ttf").get(24)
        val pixel = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixel.setColor(Color.WHITE)
        pixel.fill()
        p = Texture(pixel)
        colorGrid = Color.DARK_GRAY.cpy()
    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        //batch.drawBox(CPosition[entity].position.x,CPosition[entity].position.y,CImage[entity].width,CImage[entity].height,
               // 5f,Color.YELLOW)
    }

    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        font.draw(batch,"FPS[ ${Gdx.graphics.framesPerSecond} ]\n",camera.position.x-camera.viewportWidth/2 + 50,camera.position.y+ camera.viewportHeight/2 - 40f)
        batch.end()
    }
}