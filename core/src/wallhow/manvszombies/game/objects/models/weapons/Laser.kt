package wallhow.manvszombies.game.objects.models.weapons

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.manvszombies.game.objects.models.Player
import wallhow.manvszombies.game.objects.models.weapons.abstracts.AWeapon
import com.badlogic.gdx.utils.Array
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.image
import wallhow.acentauri.ashley.components.extension.position
import wallhow.acentauri.utils.gdxSchedule
import wallhow.manvszombies.game.Game

/**
 * Created by wallhow on 13.05.17.
 */
class Laser (val player: Player) : AWeapon(), Component {
    companion object : ComponentResolver<Laser>(Laser::class.java)
    private var isShot = false
    private var isNyanLaserCreated = false
    private var nyanLaser : Array<Entity> = Array<Entity>()
    private val nyanImage = TextureRegion(Texture("assets/nyan.png")) //TODO Вынести это в отдельный класс

    init {

    }

    override fun shot(direction : Vector2) {
        if(!isShot) {
            if(!isNyanLaserCreated) {
                isNyanLaserCreated = true
                createNyanLaser(direction)
            }
            else {
                val count = (Game.viewport.worldHeight / nyanImage.regionHeight).toInt()
                val d = direction.sub(CPosition[player].position).nor()
                val vec = Vector2(CPosition[player].position.x+d.x*count,
                        CPosition[player].position.y+d.y*count)
                nyanLaser.forEach {
                    it.position.set(vec)
                    it.image.rotation = vec.angle() - 90
                }
            }
        }
    }

    private fun createNyanLaser(direction : Vector2) {
        val count = (Game.viewport.worldHeight/nyanImage.regionHeight).toInt()
        val d = direction.sub(CPosition[player].position).nor()
        val vec = Vector2(CPosition[player].position.x+d.x*count,
                CPosition[player].position.y+d.y*count)
        for (i in 0..count-1) {
            val pos = CPosition(vec)
            val image = CImage(nyanImage).apply {
                this.rotation = vec.angle() - 90
            }

            val laserSegment = Entity().apply {
                add(image)
                add(pos)
            }

            nyanLaser.add(laserSegment)
            Game.engine.addEntity(laserSegment)
        }
        gdxSchedule(5f) { // TODO Установить настройку времени
            isNyanLaserCreated = false
            nyanLaser.forEach {
                Game.engine.removeEntity(it)
            }
            nyanLaser.clear()
        }
    }
}