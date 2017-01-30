package wallhow.manvszombies.game.components

import com.badlogic.ashley.core.Component
import wallhow.acentauri.ashley.components.ComponentResolver

/**
 * Created by wallhow on 22.01.17.
 */
class CHealth(var maxHealth: Float,var currentHealth: Float = maxHealth) : Component {
    companion object : ComponentResolver<CHealth>(CHealth::class.java)
    var draw = true
}