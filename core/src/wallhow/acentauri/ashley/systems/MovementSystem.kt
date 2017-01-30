package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.*
import wallhow.acentauri.ashley.components.extension.movementComponent
import wallhow.acentauri.ashley.components.extension.position

/**
 * Created by wallhow on 12.12.16.
 */
class MovementSystem @Inject constructor()  : IteratingSystem(Family.all(CMovement::class.java, CPosition::class.java).get()) {


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity.position
        val move = entity.movementComponent

        position.add(move.velocity.x*deltaTime,move.velocity.y*deltaTime)
    }
}