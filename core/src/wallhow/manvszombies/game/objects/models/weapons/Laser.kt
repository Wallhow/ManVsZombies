package wallhow.manvszombies.game.objects.models.weapons

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.objects.models.Player
import wallhow.manvszombies.game.objects.models.weapons.abstracts.AWeapon
import com.badlogic.gdx.utils.Array
import wallhow.acentauri.ashley.action.ActionSequence
import wallhow.acentauri.ashley.action.CAction
import wallhow.acentauri.ashley.action.scaleTo
import wallhow.acentauri.ashley.action.xPositionTo
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.*
import wallhow.acentauri.extension.isTrue
import wallhow.acentauri.utils.gdxSchedule
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.log
import wallhow.manvszombies.game.objects.models.ZombieBlue
import wallhow.manvszombies.game.objects.models.ZombieGreen
import wallhow.manvszombies.game.objects.models.ZombieRed
import wallhow.manvszombies.game.objects.models.weapons.abstracts.Weapon
import wallhow.manvszombies.game.objects.models.weapons.abstracts.buildCharacteristic
import wallhow.manvszombies.game.systems.KickMobSystem
import javax.inject.Inject

/**
 * Created by wallhow on 13.05.17.
 */
class Laser (val player: Player) : AWeapon(), Component {
    companion object : ComponentResolver<Laser>(Laser::class.java)
    var isShot = false
    private var isNyanLaserCreated = false
    private var nyanLaser : Entity = Entity()
    var nyanLaserSegments : Array<Entity> = Array()
    private val nyanImage = TextureRegion(Texture("assets/nyan.png")) //TODO Вынести это в отдельный класс

    private var amplitude = 3f //Амплитуда колебания луча
    private var v = 0.1f //Частота колебания импульса луча
    init {
        add(this)
    }

    override fun shot(direction : Vector2,point: Vector2) {
        if(!isShot) {
            if(!isNyanLaserCreated) {
                createNyanLaser(direction,point)
            }
        }
        else {
            moveNyanLaser(direction)
        }
    }

    override var isReload: Boolean = false
    override fun reload() {
        isReload = true
    }

    private var characteristic = buildCharacteristic(
            timeReload = 1.0f,
            maxCharge = 1,
            speedCharge = 500f,
            fireRet = 5f,
            damage = 10f
    )
    override fun characteristic(): Weapon.Characteristic {
        return characteristic
    }

    override var currentCharge: Int = 1
    private fun moveNyanLaser(direction: Vector2) {
        if(isNyanLaserCreated) {
            val angle = direction.angle()
            nyanLaserSegments.forEachIndexed { i, it ->
                it.image.rotation = angle- 90
                it.position.set((it.image.height + it.image.halfHeight) * i, 0f)
                it.position.rotate(angle)
                it.position.x += player.position.x + player.image.halfWidth - it.image.halfWidth
                CAction[it].sequence.getAction().restart(it.position.x,
                        if (i % 2 == 0) it.position.x - amplitude else it.position.x + amplitude)
            }
        }
    }
    private fun createNyanLaser(direction: Vector2,point: Vector2) {
        if(!isNyanLaserCreated) {
            isNyanLaserCreated = true
            isShot = true

            val angle = direction.angle()
            val count = (Game.viewport.worldHeight/(nyanImage.regionHeight*1.8f)).toInt()

            (0..count).forEach {
                val img = CImage(nyanImage).apply {
                    scale = 1.8f
                    rotation = direction.angle() - 90
                }

                val position = Vector2((img.height + img.halfHeight) * it, 0f)
                position.rotate(angle)
                        .x += player.position.x + player.image.halfWidth - img.halfWidth
                val segment = Entity().apply {
                    add(img)
                    add(CPosition(position))
                    val action = CAction(false)
                    action.sequence.xPositionTo(CPosition[this],
                            if (it % 2 == 0) this.position.x - amplitude else this.position.x + amplitude,
                            v, true).start()
                    add(action)
                }
                nyanLaserSegments.add(segment)
                Game.engine.addEntity(segment)

                gdxSchedule(characteristic.fireRet) {
                    isNyanLaserCreated = false
                    isShot = false
                    nyanLaserSegments.forEach {
                        Game.engine.removeEntity(it)
                    }
                    nyanLaserSegments.clear()
                }
            }
        }
    }
}

class LaserWeaponSystem : IteratingSystem(Family.all(Laser::class.java).get()) {
    init {
        log("laserWeaponSystem added")
    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        Laser[entity].isShot.isTrue {
            log(entity)
            Laser[entity].nyanLaserSegments.forEach { segment ->
                log("segment ${segment.position}")
                KickMobSystem.checkCollideAndKick(engine.getEntitiesFor(Family.all(ZombieGreen::class.java).get()),
                        segment,
                        Laser[entity].characteristic().damage)?.let {
                    log(it)
                }

                KickMobSystem.checkCollideAndKick(engine.getEntitiesFor(Family.all(ZombieBlue::class.java).get()),
                        segment,
                        Laser[entity].characteristic().damage)
                KickMobSystem.checkCollideAndKick(engine.getEntitiesFor(Family.all(ZombieRed::class.java).get()),
                        segment,
                        Laser[entity].characteristic().damage)
            }
        }
    }
}