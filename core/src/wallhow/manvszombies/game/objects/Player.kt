package wallhow.manvszombies.game.objects

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CMovement
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.position
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CGun
import wallhow.manvszombies.game.components.CKickMob

/**
 * Created by wallhow on 22.01.17.
 */
class Player : Entity() {
    init {
        val view = Game.viewport
        add(CImage(Game.atlas.findRegion("player")).apply { scale = 1.2f })
        add(CPosition(Vector2(view.worldWidth/2-CImage[this].width/2,0f-CImage[this].height/2)).apply {
            zIndex = -position.y.toInt()
        })
        add(CGun())
    }

    fun see(x: Float,y : Float) {
        val d = Vector2(x,y).sub(CPosition[this].position.x+CImage[this].width/2,
                CPosition[this].position.y+CImage[this].height/2).nor()
        val a = d.angle()
        println(a-90)
        CImage[this].rotation = a - 90
        shot(d)
    }

    fun shot (direction: Vector2) {
        if(!CGun[this].reload) {
            val bullet = Entity()
            bullet.add(CImage(Game.atlas.findRegion("bullet")).apply {
                scale = 1.5f
                when (CGun[this@Player].gunType) {
                    GunType.GREEN -> {color = Color.GREEN}
                    GunType.BLUE -> {color = Color.BLUE}
                    GunType.RED -> {color = Color.RED;scale = 1.8f}
                }
            })
            bullet.add(CPosition(position.cpy().add(halfSize).sub(bullet.halfSize)))
            bullet.add(CMovement(direction.scl(CGun[this].speed)))
            CGun[this].bullets--
            bullet.add(CKickMob(CGun[this].gunType))
            Game.engine.addEntity(bullet)
        }
    }
}

enum class GunType(var timeReload: Float, var force: Float, val mobType: TypeZombie) {
    GREEN(1f,1f,TypeZombie.GREEN),
    BLUE(1f,1f,TypeZombie.BLUE),
    RED(1.5f,1.7f,TypeZombie.RED)
}
