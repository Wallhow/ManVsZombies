package wallhow.acentauri.ashley.action

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

/**
 * Created by wallhow on 23.05.17.
 */
class ActionSystem : IteratingSystem(Family.all(CAction::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val action = CAction[entity]
        action.sequence.getAction().let {
            it.act(deltaTime)
            if (it.isReady() && it.isRepeat()) {
                it.restart()
            } else if(it.isReady()) {
                action.sequence.nextSequence()
            }
        }
    }
}