package wallhow.manvszombies.game.objects.models.gun

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CMovement
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.position
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.models.gun.CGun
import wallhow.manvszombies.game.components.CKickMob
import wallhow.manvszombies.game.objects.models.Player

class Gun(val player: Player) : Entity() {
    init {
        add(CGun())
    }
    fun shot (direction: Vector2) {
        if(!CGun[this].reload) {
            CGun[this].shot = true
            val bullet = Entity()
            bullet.add(CImage(Game.atlas.findRegion("bullet")).apply {
                scale = 1.5f
                when (CGun[this@Gun].gunType) {
                    GunType.GREEN -> {color = Color.GREEN}
                    GunType.BLUE -> {color = Color.BLUE}
                    GunType.RED -> {color = Color.RED;scale = 1.8f}
                }
            })
            bullet.add(CPosition(player.position.cpy().add(player.halfSize).sub(bullet.halfSize)))
            bullet.add(CMovement(direction.scl(CGun[this].speed)))
            bullet.add(CKickMob(CGun[this].gunType))
            Game.engine.addEntity(bullet)
        }
    }
}