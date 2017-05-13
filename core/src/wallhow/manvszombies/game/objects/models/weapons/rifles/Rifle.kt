package wallhow.manvszombies.game.objects.models.weapons.rifles

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CMovement
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.halfSize
import wallhow.acentauri.ashley.components.extension.position
import wallhow.acentauri.utils.gdxSchedule
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CKickMob
import wallhow.manvszombies.game.objects.models.Player
import wallhow.manvszombies.game.objects.models.gun.GunType
import wallhow.manvszombies.game.objects.models.weapons.abstracts.AWeapon
import wallhow.manvszombies.game.objects.models.weapons.abstracts.buildCharacteristic

/**
 * Created by wallhow on 13.05.17.
 */

// Винтовка :D А то хреново с инглишОм
open class Rifle(val player: Player) : AWeapon() {
    protected val characteristic = buildCharacteristic(
            timeReload = 1.0f,
            maxCharge = 15,
            speedCharge = 500f,
            fireRet = 0.25f,
            damage = 5f
    )
    override var currentCharge : Int = characteristic.maxCharge
    override var isReload : Boolean = false
    private var isShot = false

    protected var bulletColor : Color = Color.CYAN
    protected var bulletScale : Float = 1.5f
    protected var gunType = GunType.GREEN

    override fun shot(direction: Vector2) {
        if(!isReload && !isShot) {
            isShot = true
            gdxSchedule(characteristic.fireRet) {
                isShot = false
            }
            val bullet = Entity()
            bullet.add(CImage(Game.Companion.atlas.findRegion("bullet")).apply {
                scale = bulletScale
                color = bulletColor
            })
            bullet.add(CPosition(player.position.cpy().add(player.halfSize).sub(bullet.halfSize)))
            bullet.add(CMovement(direction.scl(characteristic.speedCharge)))
            bullet.add(CKickMob(gunType))
            Game.engine.addEntity(bullet)
        }
    }
    override fun reload() {
    }
    override fun characteristic() = characteristic
}