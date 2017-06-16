package wallhow.manvszombies.game.objects.models

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.*
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.log
import wallhow.manvszombies.game.objects.models.weapons.*
import wallhow.manvszombies.game.objects.models.weapons.abstracts.Weapon
import wallhow.manvszombies.game.objects.models.weapons.rifles.RifleBlue
import wallhow.manvszombies.game.objects.models.weapons.rifles.RifleGreen
import wallhow.manvszombies.game.objects.models.weapons.rifles.RifleRed
import java.util.*

/**
 * Created by wallhow on 22.01.17.
 */
class Player(position: Vector2) : Entity() {
    var weapon : Weapon
    var weapons : Map<WeaponsType, Weapon>

    private val pointClick = Vector2()
    private val directionSee = Vector2()

    init {
        add(CImage(Game.atlas.findRegion("player")).apply { scale = 1.2f })
        add(CPosition(position.sub(CImage[this].halfWidth,CImage[this].halfHeight)).apply {
            zIndex = -position.y.toInt()
        })

        weapons = HashMap<WeaponsType, Weapon>().apply {
            put(WeaponsType.RifleGreen, RifleGreen(this@Player))
            put(WeaponsType.RifleRed, RifleRed(this@Player))
            put(WeaponsType.RifleBlue, RifleBlue(this@Player))
            put(WeaponsType.NyanLaser, Laser(this@Player))
        }
        weapon = weapons[WeaponsType.NyanLaser] as Laser
        Game.engine.addEntity(weapon as Entity)
    }

    fun see(x: Float,y : Float) {
        log("see x=$x y=$y")
        directionSee.set(x,y).sub(position.x+image.halfWidth,position.y+image.halfHeight).nor()

        CImage[this].rotation = directionSee.angle() - 90

        pointClick.set(x,y)
        weapon.shot(directionSee,pointClick)
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
        weapon = weapons[type] as Weapon
        Game.engine.addEntity(weapon as Entity)
    }
}

