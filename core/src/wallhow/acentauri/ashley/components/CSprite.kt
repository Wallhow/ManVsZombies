package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite

/**
 * Created by wallhow on 03.04.17.
 */
data class CSprite (var image: Sprite) : Component {
    companion object : ComponentResolver<CSprite>(CSprite::class.java)
}