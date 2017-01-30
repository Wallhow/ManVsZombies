package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class CMovement(var velocity: Vector2): Component {
    companion object : ComponentResolver<CMovement>(CMovement::class.java)
}