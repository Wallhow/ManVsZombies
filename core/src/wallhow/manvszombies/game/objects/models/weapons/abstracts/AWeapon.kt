package wallhow.manvszombies.game.objects.models.weapons.abstracts

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

open class AWeapon : Entity(), Weapon {
    init {

    }

    override fun shot(direction: Vector2) { }

    override fun reload() { }

    override var isReload: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var currentCharge: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun characteristic(): Weapon.Characteristic {
        TODO("not implemented")
    }
}