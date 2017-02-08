package wallhow.manvszombies.game.objects.models.gun

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.manvszombies.game.objects.models.gun.CGun
import wallhow.manvszombies.game.components.task
import wallhow.manvszombies.game.objects.models.gun.GunType

/**
 * Created by wallhow on 22.01.17.
 */
class GunSystem @Inject constructor() : IteratingSystem(Family.all(CGun::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val gun = CGun[entity]

        if(gun.shot) {
            when (gun.gunType) {
                GunType.GREEN -> { gun.bullets -- }
                GunType.BLUE -> { gun.bullets-- }
                GunType.RED -> { gun.bullets -=3}
            }
            gun.shot = false
        }

        if(gun.bullets<=0 && !gun.reload && gun.gunType != GunType.RED) {
            setReloadGun(gun,entity)
        }
        if(gun.bullets<3 && !gun.reload && gun.gunType == GunType.RED) {
            setReloadGun(gun,entity)
        }
        if(gun.reload) {

        }
    }

    private fun setReloadGun(gun:CGun,entity: Entity) {
        gun.reload = true

        entity.task(gun.gunType.timeReload) {
            CGun[it].reload = false
            CGun[it].bullets = CGun[it].max_bullets
            println("reloaded gun")
        }
    }
}