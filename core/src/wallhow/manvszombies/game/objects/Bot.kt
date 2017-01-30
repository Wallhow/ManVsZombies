package wallhow.manvszombies.game.objects

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CMovement
import wallhow.acentauri.ashley.components.CPosition

/**
 * Created by wallhow on 13.01.17.
 */
open class Bot : Entity() {
    init {

    }

    protected fun createPosition(position: Vector2) {
        add(CPosition(position,-position.y.toInt()))
    }
    protected fun createImage(textureRegion: TextureRegion,frameWidth: Float, frameHeight: Float, sequence : IntArray) {
        val cImage = CImage(textureRegion,frameWidth,frameHeight,frameWidth,frameHeight)
        cImage.frameSequence = sequence
        add(cImage)
    }
    protected fun createMovement(direction: Vector2,speed: Float) {
        add(CMovement(direction.scl(speed,speed)))
    }
}