package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import wallhow.manvszombies.game.components.actions.CAction
import wallhow.manvszombies.game.components.actions.CColorAction
import wallhow.manvszombies.game.components.actions.CDeletAction
import wallhow.manvszombies.game.components.actions.CSequenceAction

/**
 * Created by wallhow on 22.01.17.
 */
class  ActionsSystem : IteratingSystem(Family.one(CSequenceAction::class.java).get()){
    override fun processEntity(entity: Entity, deltaTime: Float) {
        CSequenceAction[entity].update(entity,deltaTime)
    }
}