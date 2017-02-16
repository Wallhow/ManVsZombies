package wallhow.manvszombies.game.objects

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.components.CHealth

class Cell(defType: Type) : Entity() {
    val objects: Array<Entity>
    val cellComponent: CellComponent

    init {
        objects = Array()
        cellComponent = CellComponent(defType)
        add(cellComponent)
        add(CHealth(defType.startHealth))
    }

    class CellComponent(val type: Type) : Component {
        companion object : ComponentResolver<CellComponent>(CellComponent::class.java)

    }

    enum class Type(var startHealth: Float, val size: Vector2) {
        T4X4(Balance.FieldHpV3.toFloat(), Vector2(4f, 4f)),
        T2X2(Balance.FieldHpV2.toFloat(), Vector2(2f, 2f)),
        T1X1(Balance.FieldHpV1.toFloat(), Vector2(1f, 1f)),
        TYPE_5(15f, Vector2(5f, 3f))
    }
}