package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.components.CTimer
import wallhow.manvszombies.game.components.CGun

/**
 * Created by wallhow on 22.01.17.
 */
class GunSystem @Inject constructor() : IteratingSystem(Family.all(CGun::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val gun = CGun[entity]

        if(gun.bullets<=0 && !gun.reload) {
            gun.reload = true
            entity.add(CTimer(gun.gunType.timeReload) {
                gun.reload = false
                gun.bullets=gun.max_bullets
                println("reloaded")
            })
        }
        if(gun.reload) {

        }
    }
}