package wallhow.manvszombies.game.components

import com.badlogic.ashley.core.Component
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.objects.GunType

class CGun : Component {
    companion object : ComponentResolver<CGun>(CGun::class.java)
    var gunType: GunType = GunType.GREEN
    var bullets = 15
    var max_bullets = 15
    var reload = false
    var speed = 450f
}