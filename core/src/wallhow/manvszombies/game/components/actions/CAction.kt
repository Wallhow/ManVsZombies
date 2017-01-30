package wallhow.manvszombies.game.components.actions

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import wallhow.acentauri.ashley.components.ComponentResolver

/**
 * Created by wallhow on 22.01.17.
 */
interface CAction : Component {
    companion object : ComponentResolver<CAction>(CAction::class.java)
    var currentTime:Float
    var ready : Boolean
    fun update(entity: Entity,delta: Float)
}