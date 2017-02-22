package wallhow.acentauri.extension.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * Created by wallhow on 21.02.17.
 */

/**
 * @param pixel : Текстура которой будет нарисована сетка
 * @param width : Ширина сетки
 * @param height : Высота сетки
 * @param cellSize : Размер ячейки сетки
 * @param color : Цвет сетки
 * @param sizeLine : Размер линии сетки
 */
fun SpriteBatch.drawGridLine(pixel: Texture, width: Float, height: Float, cellSize:Float, sizeLine : Float = 1f, color: Color = Color.LIGHT_GRAY) {
    val w = (width / cellSize).toInt()
    val h = (height / cellSize).toInt()
    this.color = color
    for(x in 0..w) {
        this.draw(pixel,x*cellSize,0f,sizeLine,height)
    }
    for ( y in 0..h) {
        this.draw(pixel,0f,y*cellSize,width,sizeLine)
    }
    this.color = Color.CLEAR
}
/**
 * @param pixel : Текстура которой будет нарисована сетка
 * @param width : Ширина сетки
 * @param height : Высота сетки
 * @param cellSize : Размер ячейки сетки
 * @param color : Цвет сетки
 * @param sizeLine : Размер линии сетки
 */
fun SpriteBatch.drawGridLine(pixel: TextureRegion, width: Float, height: Float, cellSize:Float, sizeLine : Float = 1f, color: Color = Color.LIGHT_GRAY) {
    val w = (width / cellSize).toInt()
    val h = (height / cellSize).toInt()
    this.color = color
    for(x in 0..w) {
        this.draw(pixel,x*cellSize,0f,sizeLine,height)
    }
    for ( y in 0..h) {
        this.draw(pixel,0f,y*cellSize,width,sizeLine)
    }
    this.color = Color.CLEAR
}


fun SpriteBatch.drawBox(pixel: Texture,x:Float,y:Float,width: Float,height: Float,size: Float = 1f, color: Color = Color.LIGHT_GRAY) {
    val halfSize= size/2
    this.color = color
    this.draw(pixel,x-halfSize,y-halfSize,width+size,size)
    this.draw(pixel,x-halfSize,y+height-halfSize,width+size,size)
    this.draw(pixel,x-halfSize+width,y-halfSize,size,height+size)
    this.draw(pixel,x-halfSize,y-halfSize,size,height+size)
    this.color = Color.WHITE
}
fun SpriteBatch.drawBox(pixel: TextureRegion,x:Float,y:Float,width: Float,height: Float,size: Float = 1f, color: Color = Color.LIGHT_GRAY) {
    val halfSize= size/2
    this.color = color
    this.draw(pixel,x-halfSize,y-halfSize,width+size,size)
    this.draw(pixel,x-halfSize,y+height-halfSize,width+size,size)
    this.draw(pixel,x-halfSize+width,y-halfSize,size,height+size)
    this.draw(pixel,x-halfSize,y-halfSize,size,height+size)
    this.color = Color.WHITE
}