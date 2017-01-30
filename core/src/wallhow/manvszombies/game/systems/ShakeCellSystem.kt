package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.acentauri.ashley.components.extension.position
import wallhow.manvszombies.game.components.CTimer

/**
 * Created by naxo1 on 28.01.2017.
 */
class ShakeCellSystem @Inject constructor() : IteratingSystem(Family.all(CShake::class.java).get()){
    override fun processEntity(entity: Entity, deltaTime: Float) {
        if(CShake[entity].dirty) {
            CShake[entity].prePosition = entity.position.cpy()
            CShake[entity].dirty = false
            entity.add(CTimer(2f) {
                entity.position.set(CShake[entity].prePosition)
                entity.remove(CShake::class.java)
            })
        }

        entity.position.x = CShake[entity].prePosition.x
        entity.position.y = CShake[entity].prePosition.y

        val xShake = MathUtils.random(-4,4)
        val yShake = MathUtils.random(-4,4)

        entity.position.x+=xShake
        entity.position.y+=yShake

    }
}

class CShake : Component{
    var dirty = true
    var prePosition = Vector2()
    companion object : ComponentResolver<CShake>(CShake::class.java)
}
