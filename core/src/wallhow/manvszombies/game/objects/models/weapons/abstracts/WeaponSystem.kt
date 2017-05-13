package wallhow.manvszombies.game.objects.models.weapons.abstracts

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.manvszombies.game.components.task

/**
 * Created by wallhow on 13.05.17.
 */
class WeaponSystem @Inject constructor() : IteratingSystem(Family.all(Weapon::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val weapon = Weapon[entity]

        if(weapon.currentCharge<=0 && !weapon.isReload) {
            setReloadGun(weapon,entity)
        }
    }

    private fun setReloadGun(weapon: Weapon, entity: Entity) {
        weapon.isReload = true

        entity.task(weapon.characteristic().timeReload) {
            weapon.isReload = false
            weapon.currentCharge = weapon.characteristic().maxCharge
            println("reloaded gun")
        }
    }
}