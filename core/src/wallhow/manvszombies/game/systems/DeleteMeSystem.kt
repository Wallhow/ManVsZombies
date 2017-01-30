package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.manvszombies.game.components.DeleteMe

/**
 * Created by wallhow on 22.01.17.
 */
class DeleteMeSystem @Inject constructor() : IteratingSystem(Family.all(DeleteMe::class.java).get()){
    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.removeEntity(entity)
    }
}