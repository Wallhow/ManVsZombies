package wallhow.manvszombies.game.components.actions

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import wallhow.acentauri.ashley.components.extension.color
import wallhow.manvszombies.game.components.DeleteMe

/**
 * Created by wallhow on 22.01.17.
 */
class CDeletAction(val time: Float) : CAction {
    override var currentTime: Float = 0f
    override var ready: Boolean = false

    override fun update(entity: Entity, delta: Float) {
        currentTime+=delta
        if(currentTime>=time) {
            entity.add(DeleteMe())
        }
    }
}