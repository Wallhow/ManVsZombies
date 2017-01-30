package wallhow.ru.game.components.interfaces

import com.badlogic.ashley.core.Entity

interface CollideListener {
    fun beginCollide(entity: Entity)
    fun endCollide(entity: Entity)
}