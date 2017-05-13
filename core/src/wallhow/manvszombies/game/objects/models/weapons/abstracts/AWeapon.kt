package wallhow.manvszombies.game.objects.models.weapons.abstracts

import com.badlogic.ashley.core.Entity

abstract class AWeapon : Entity(), Weapon {
    init {
        add(this)
    }
}