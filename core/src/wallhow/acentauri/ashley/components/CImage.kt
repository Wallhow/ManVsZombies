package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

/**
 * Created by wallhow on 09.12.16.
 */
class CImage(var texture: TextureRegion,
             private val width_: Float = texture.regionWidth.toFloat(),private val height_: Float = texture.regionHeight.toFloat(),
             var frameWidth: Float = 0f, var frameHeight: Float = 0f) : Component {
    companion object : ComponentResolver<CImage>(CImage::class.java)

    var scaleX = 1f
    var scaleY = 1f
    var rotation = 0f
    var timeBetweenFrames = 0.1f // задержка между кадрами
    val frameCount: Int // кол-во кадров всего
    var frameSequence : IntArray
    var color: Color = Color.WHITE.cpy()
    var time = 0.0f
    private var originX_ = width / 2
    private var originY_ = height / 2
    private var textureFrames: Array<TextureRegion> = Array() // кадры анимации
    private var currentFrame = 0 // Текущий кадр
    private val countFrameWidth: Int // кол-во кадров по горизонтали
    private val countFrameHeight : Int // кол-во кадров по вертикали
    private var animation = false

    init {
        if(frameWidth != 0f || frameHeight != 0f) {
            countFrameWidth = (texture.regionWidth / frameWidth).toInt()
            countFrameHeight = (texture.regionHeight / frameHeight).toInt()

            for ( textures in texture.split(frameWidth.toInt(),frameHeight.toInt())) {
                for ( frame in textures) {
                    textureFrames.add(frame)
                }
            }
        }
        else {
            countFrameWidth = 1
            countFrameHeight = 1
            textureFrames.add(texture)
            frameWidth = width
            frameHeight = height
        }
        frameCount = (countFrameWidth * countFrameHeight)

        if(frameCount==1) {
            frameSequence =  intArrayOf(0)
        }else
            frameSequence =  IntRange(0,frameCount-1).toList().toIntArray()

    }


    fun getFrame() = textureFrames.get(frameSequence[currentFrame])
    fun nextFrame() {
        if(time >= timeBetweenFrames) {
            currentFrame++
            if(currentFrame > frameSequence.size-1) {
                currentFrame = 0
            }
            time = 0f
        }
    }
    fun prefFrame() {
        if(time >= timeBetweenFrames) {
            currentFrame--
            if(currentFrame < 0) {
                currentFrame = frameSequence.size-1
            }
            time = 0f
        }
    }
    fun setFrame(frame: Int) {
        currentFrame = frame
    }

    fun play() { animation = true }
    fun pause() { animation = false }
    fun end() { animation = false; reset() }
    private fun reset() {
        currentFrame = 0
        time = 0f
    }
    fun isPlay() = animation

    var scale : Float
        set(value) { scaleX = value; scaleY = value }
        get() { return if(scaleX==scaleY) scaleX else 1f }
    val width : Float
        get() = width_*scale
    val height : Float
        get() = height_*scale

    var originX : Float
        get() =  width / 2f
        set(value) { originX_ = value }
    var originY : Float
        get() =  height / 2f
        set(value) { originY_ = value }

}

