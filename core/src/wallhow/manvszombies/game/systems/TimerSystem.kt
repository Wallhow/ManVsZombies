package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.manvszombies.game.components.CTimer
import wallhow.manvszombies.game.components.CGun

/**
 * Created by wallhow on 22.01.17.
 */
class TimerSystem @Inject constructor() : IteratingSystem(Family.all(CTimer::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        CTimer[entity].currentTime += deltaTime
        if (CTimer[entity].currentTime >= CTimer[entity].time) {
            CTimer[entity].apply()
            if (CTimer[entity].repeat == 0) {
                entity.remove(CTimer::class.java)
            } else {
                CTimer[entity].currentTime = CTimer[entity].time
                if (CTimer[entity].repeat > 0)
                    CTimer[entity].repeat--
            }
        }
    }
}