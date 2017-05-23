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
import wallhow.acentauri.extension.log
import wallhow.acentauri.utils.gdxSchedule
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.models.weapons.abstracts.Weapon
import wallhow.manvszombies.game.objects.models.weapons.abstracts.buildCharacteristic
import javax.inject.Inject

/**
 * Created by wallhow on 13.05.17.
 */
class Laser (val player: Player) : AWeapon(), Component {
    companion object : ComponentResolver<Laser>(Laser::class.java)
    private var isShot = false
    private var isNyanLaserCreated = false
    private var nyanLaser : Entity = Entity()
    private val nyanImage = TextureRegion(Texture("assets/nyan.png")) //TODO Вынести это в отдельный класс

    init {

    }

    override fun shot(direction : Vector2,point: Vector2) {
        if(!isShot) {
            if(!isNyanLaserCreated) {
                //isNyanLaserCreated = true
                //createNyanLaser(direction)
                testCreateNyanLaser(direction,point)
            }
            else {
                moveNyanLaser(direction)
            }
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
            fireRet = 0.15f,
            damage = 5f
    )
    override fun characteristic(): Weapon.Characteristic {
        return characteristic
    }

    override var currentCharge: Int = 1

    private fun moveNyanLaser(direction: Vector2) {
        nyanLaser.image.rotation = direction.angle() - 90
    }

    private fun testCreateNyanLaser(direction: Vector2,point: Vector2) {
        val a = direction.angle()
        //(Game.viewport.worldHeight/nyanImage.regionHeight*1.5f).toInt()
        (0..(Game.viewport.worldHeight/nyanImage.regionHeight*1.5f).toInt()).forEach {
            val img = CImage(nyanImage).apply {
                scale = 1.8f
                rotation = direction.angle()-90
            }

            val position = Vector2((img.height+img.halfHeight)*it,
                    0f)
            val angle = a
            position.rotate(angle)
                    .x+=player.position.x+player.image.halfWidth - img.halfWidth

            Game.engine.addEntity(Entity().apply {
                add(img)
                add(CPosition(position))
                val action = CAction(false)
                action.sequence.xPositionTo(CPosition[this],
                        if(it%2==0) this.position.x-2f else this.position.x+2f,
                        0.1f,true).start()
                add(action)
            })
        }
    }

    private fun createNyanLaser(direction : Vector2) {
        val image = CImage(nyanImage,nyanImage.regionWidth.toFloat(),Game.viewport.worldHeight).apply {
            scale = 1.2f
            originY = 0f
            this.rotation = direction.angle() - 90
        }
        val pos = CPosition(player.position.cpy().add(player.width/2,player.height/2f)
                .sub(image.halfWidth,0f))

        nyanLaser = Entity().apply {
            add(image)
            add(pos)
            add(this@Laser)
            add(scaleBy(1f,0.20f,true))
        }


        Game.engine.addEntity(nyanLaser)

        gdxSchedule(5f) { // TODO Установить настройку времени
            isNyanLaserCreated = false
            nyanLaser.deleteMe()
        }
    }
}
data class ScaleAction(val timeAction: Float, val keys: kotlin.Array<ActionKey>,
                       val repeat: Boolean,
                       var currentTime: Float = 0f) : Component {
    companion object : ComponentResolver<ScaleAction>(ScaleAction::class.java)
}
data class ActionKey(var data: Float,val timeStep: Float,var current : Float = 0f)

fun scaleBy(scale : Float, time: Float, repeat: Boolean) : ScaleAction {
    return ScaleAction(time, arrayOf(ActionKey(scale,0f,0f)),repeat)
}

class LaserWeaponSystem @Inject constructor() : IteratingSystem(Family.all(Laser::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        ScaleAction[entity].apply {
            currentTime+=deltaTime
            if(currentTime>=timeAction) {
                if(entity.image.scaleX == 0.95f) {
                    entity.image.scaleX = 1.2f
                }
                else {
                    entity.image.scaleX = 0.95f
                }
                if(repeat) {
                    currentTime = 0f
                }
                else {
                    entity.deleteMe()
                }
            }
        }
    }
}