package wallhow.manvszombies.game.objects.models

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
import wallhow.manvszombies.game.objects.models.gun.CGun
import wallhow.manvszombies.game.components.CKickMob
import wallhow.manvszombies.game.objects.Balance
import wallhow.manvszombies.game.objects.models.gun.Gun
import wallhow.manvszombies.game.objects.models.gun.GunType
import wallhow.manvszombies.game.objects.models.weapons.*
import wallhow.manvszombies.game.objects.models.weapons.abstracts.AWeapon
import wallhow.manvszombies.game.objects.models.weapons.abstracts.Weapon
import wallhow.manvszombies.game.objects.models.weapons.rifles.RifleBlue
import wallhow.manvszombies.game.objects.models.weapons.rifles.RifleGreen
import wallhow.manvszombies.game.objects.models.weapons.rifles.RifleRed
import java.util.*

/**
 * Created by wallhow on 22.01.17.
 */
class Player(position: Vector2) : Entity() {
    val gun: Gun

    var weapon : Weapon

    var weapons : Map<WeaponsType, Weapon>
    init {
        add(CImage(Game.atlas.findRegion("player")).apply { scale = 1.2f })
        add(CPosition(position.sub(CImage[this].width/2,CImage[this].height/2)).apply {
            zIndex = -position.y.toInt()
        })
        gun = Gun(this)

        weapon = RifleGreen(this)

        weapons = HashMap<WeaponsType, Weapon>().apply {
            put(WeaponsType.RifleGreen, RifleGreen(this@Player))
            put(WeaponsType.RifleRed, RifleRed(this@Player))
            put(WeaponsType.RifleBlue, RifleBlue(this@Player))
        }

        Game.engine.addEntity(weapon as Entity)
        Game.engine.addEntity(gun)
    }

    fun see(x: Float,y : Float) {
        val d = Vector2(x,y).sub(CPosition[this].position.x+ CImage[this].width/2,
                CPosition[this].position.y+ CImage[this].height/2).nor()
        val a = d.angle()
        CImage[this].rotation = a - 90
        gun.shot(d)

        weapon.shot(d)
    }

    fun getMaxCountBullet() : Int {
        return weapon.characteristic().maxCharge
    }
    fun getCountBullet() : Int {
        return  weapon.currentCharge
    }


    fun takeRedGun() {
        setWeapon(WeaponsType.RifleRed)
    }
    fun takeGreenGun() {
        setWeapon(WeaponsType.RifleGreen)
    }
    fun takeBlueGun() {
        setWeapon(WeaponsType.RifleBlue)
    }

    private fun setWeapon(type: WeaponsType) {
        Game.engine.removeEntity(weapon as Entity)
        weapon = weapons[type] as AWeapon
        Game.engine.addEntity(weapon as Entity)
    }
}
