package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.*
import wallhow.acentauri.ashley.components.extension.image
import wallhow.acentauri.ashley.components.extension.position


/**
 * Created by wallhow on 12.12.16.
 */
class InvisibleSystem @Inject constructor(val camera: Camera): IteratingSystem(Family.all(CPosition::class.java, CImage::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val camX = camera.position.x - camera.viewportWidth * 0.5f
        val camY = camera.position.y - camera.viewportHeight * 0.5f
        val camWidth = camera.position.x + camera.viewportWidth
        val camHeight = camera.position.y + camera.viewportHeight

        val ePos = entity.position
        val eWidth = entity.image.width
        val eHeight = entity.image.height

        if(ePos.x+eWidth < camX || ePos.x > camWidth ||
        ePos.y+eHeight < camY || ePos.y> camHeight) {
            if(CInvisible[entity]==null)
                entity.add(CInvisible())
        }
        else {
            if(CInvisible[entity]!=null)
                entity.remove(CInvisible::class.java)
        }
    }
}