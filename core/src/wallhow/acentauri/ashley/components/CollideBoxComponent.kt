package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import wallhow.ru.game.components.interfaces.CollideListener

class CollideBoxComponent(var widthBox: Float, var heightBox: Float,val passable: Int = 0) : Component {
    var collideListener: CollideListener = object : CollideListener {
        override fun beginCollide(entity: Entity) {
        }

        override fun endCollide(entity: Entity) {
        }
    }
    companion object : ComponentResolver<CollideBoxComponent>(CollideBoxComponent::class.java)

}