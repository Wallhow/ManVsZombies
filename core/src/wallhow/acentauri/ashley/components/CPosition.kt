package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class CPosition(val position: Vector2, var zIndex : Int = Int.MIN_VALUE) : Component {
    companion object : ComponentResolver<CPosition>(CPosition::class.java)
    private var saveX = 0f
    private var saveY = 0f
    fun save() {
        saveX = position.x
        saveY = position.y
    }
    fun load() {
        position.set(saveX,saveY)
    }
}