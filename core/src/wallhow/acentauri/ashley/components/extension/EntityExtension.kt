package wallhow.acentauri.ashley.components.extension

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.*
import wallhow.acentauri.ashley.components.interfaces.*

/**
 * Created by wallhow on 11.12.16.
 */
val Entity.position: Vector2
    get() = CPosition[this].position
var Entity.zIndex : Int
    get() = CPosition[this].zIndex
    set(value) {
        CPosition[this].zIndex = value}
//---------------
val Entity.movementComponent : CMovement
    get() = CMovement[this]
val Entity.velocity : Vector2
    get() = this.movementComponent.velocity

/**
 * Представление Entity
 */
val Entity.image : CImage
    get() = CImage[this]
val Entity.width : Float
    get() = this.image.width
val Entity.height : Float
    get() = this.image.height
val Entity.scale: Float
    get() = this.image.scale
val Entity.rotation: Float
    get() = this.image.rotation
var Entity.color : Color
    get() = this.image.color
    set(value) { this.image.color.set(value) }
val Entity.halfSize : Vector2
    get() = Vector2(width/2,height/2)

/**
 * Компонент спрайта
 */
val Entity.sprite : Sprite
    get() = CSprite[this].image

val CImage.halfWidth : Float
    get() = width*0.5f
val CImage.halfHeight : Float
    get() = height*0.5f
/**
 * CollideBoxComponent
 */
fun Entity.createCollideBox(widthBox: Float, heightBox: Float, passable: Int = 0) {
    add(CollideBoxComponent(widthBox, heightBox,passable))
}
val Entity.collideBoxComponent : CollideBoxComponent
    get() = CollideBoxComponent[this]


//---------------
fun <T : Component> Entity.tryGet(componentResolver: ComponentResolver<T>) : T? {
    return componentResolver.MAPPER.get(this)
}
//---------------
val Entity.controllerListener: ControllerListener
    get() = CPlayerController[this].controllerListener

fun Entity.deleteMe() {
    this.add(CDelete())
}