package wallhow.manvszombies.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.objects.models.gun.GunType

/**
 * Created by wallhow on 22.01.17.
 */
class CKick (val force : Float ,val time : Float, val entity: Entity) : Component {
    companion object : ComponentResolver<CKick>(CKick::class.java)
    var currentTime = 0f
}
class CKickMob(val gunType: GunType) : Component {
    companion object : ComponentResolver<CKickMob>(CKickMob::class.java)
}