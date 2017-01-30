package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class CPosition(val position: Vector2, var zIndex : Int = Int.MIN_VALUE) : Component {
    companion object : ComponentResolver<CPosition>(CPosition::class.java)
}