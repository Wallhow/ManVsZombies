package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.CDelete

/**
 * Created by wallhow on 22.01.17.
 */
class DeleteSystem @Inject constructor() : IteratingSystem(Family.all(CDelete::class.java).get()){
    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.removeEntity(entity)
    }
}