package wallhow.manvszombies.game.components

import com.badlogic.ashley.core.Component
import wallhow.acentauri.ashley.components.ComponentResolver

class CTimer (val time:Float,val apply : () -> Unit) : Component {
    var currentTime = 0f
    var repeat = 0
    companion object : ComponentResolver<CTimer>(CTimer::class.java)
}