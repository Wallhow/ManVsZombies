package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CHealth
import wallhow.manvszombies.game.components.CKick
import wallhow.acentauri.ashley.components.CDelete
import wallhow.manvszombies.game.objects.Cell

/**
 * Created by wallhow on 22.01.17.
 */
class KickSystem @Inject constructor() : IteratingSystem(Family.all(CKick::class.java).exclude(CDelete::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val kick = CKick[entity]
        kick.currentTime +=deltaTime
        if(kick.currentTime >= kick.time) {
            val cell = kick.entity as Cell
            CHealth[cell].currentHealth-=kick.force
            kick.currentTime = 0f
            cell.add(CShake())

            if(CHealth[cell].currentHealth<=0) {
                Game.getDeadCellSignaler().dispatch(cell)
            }
        }
    }
}