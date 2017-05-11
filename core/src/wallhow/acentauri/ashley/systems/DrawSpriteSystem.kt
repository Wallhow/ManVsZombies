package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.CSprite
import wallhow.acentauri.ashley.components.extension.position
import wallhow.acentauri.ashley.components.extension.sprite

/**
 * Created by wallhow on 03.04.17.
 */
class DrawSpriteSystem @Inject constructor(val batch: SpriteBatch): IteratingSystem(Family.all(CSprite::class.java, CPosition::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = entity.sprite
        sprite.setPosition(entity.position.x,entity.position.y)
        batch.color = sprite.color
        sprite.draw(batch)
        batch.color = Color.WHITE
    }

    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }
}