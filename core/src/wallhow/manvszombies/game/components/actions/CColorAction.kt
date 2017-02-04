package wallhow.manvszombies.game.components.actions

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.extension.color

/**
 * Created by wallhow on 22.01.17.
 */
class CColorAction(val time: Float,val color: Color) : CAction {
    override var currentTime: Float = 0f
    override var ready: Boolean = false

    override fun update(entity: Entity, delta: Float) {
        currentTime+=delta
        if(currentTime>=time) {
            CImage[entity].color=color
            ready = true
        }
    }
}