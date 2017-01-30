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
    init {
        font = TTFFont("assets/pixel.ttf").get(24)
        p = Texture(Pixmap(Gdx2DPixmap.newPixmap(1,1,Gdx2DPixmap.GDX2D_FORMAT_RGBA8888).apply { setPixel(1,1,Color.WHITE.toIntBits()) }))

    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        //batch.drawBox(CPosition[entity].position.x,CPosition[entity].position.y,CImage[entity].width,CImage[entity].height,
                //5f,Color.YELLOW)
    }

    fun SpriteBatch.drawBox(x:Float,y:Float,width: Float,height: Float,size: Float,color: Color) {
        val halfSize= size/2
        this.color = color
        this.draw(p,x-halfSize,y-halfSize,width+size,size)
        this.draw(p,x-halfSize,y+height-halfSize,width+size,size)
        this.draw(p,x-halfSize+width,y-halfSize,size,height+size)
        this.draw(p,x-halfSize,y-halfSize,size,height+size)
        this.color = Color.WHITE
    }

    override fun update(deltaTime: Float) {
        batch.begin()
        drawGrid(batch)
        super.update(deltaTime)
        font.draw(batch,"FPS[ ${Gdx.graphics.framesPerSecond} ]\n",camera.position.x-camera.viewportWidth/2 + 50,camera.position.y+ camera.viewportHeight/2 - 40f)
        batch.end()
    }

    fun drawGrid(batch: SpriteBatch) {
        val cellSize = 40
        val w = 400 / cellSize
        val h = 600 / cellSize
        batch.color = Color.GRAY
        for(x in 0..w) {
            batch.draw(p,x*cellSize.toFloat(),0f,5f,600f)
        }
        for ( y in 0..h) {
            batch.draw(p,0f,y*cellSize.toFloat(),400f,5f)
        }
        batch.color = Color.WHITE
    }
}