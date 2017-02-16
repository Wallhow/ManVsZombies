package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.ComponentResolver
import wallhow.acentauri.ashley.components.extension.position
import wallhow.manvszombies.game.components.task
import wallhow.manvszombies.game.objects.Cell

/**
 * Created by wallhow on 28.01.2017.
 */
class ShakeCellSystem @Inject constructor() : IteratingSystem(Family.all(CShake::class.java).get()){
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bots = (entity as Cell).objects
        if(CShake[entity].dirty) {
            CShake[entity].prePosition = entity.position.cpy()
            CShake[entity].dirty = false
            bots.forEach { CPosition[it].save() }

            entity.task(2f) {
                it.position.set(CShake[it].prePosition)
                it.remove(CShake::class.java)
                bots.forEach { CPosition[it].load() }
            }
        }
        entity.position.x = CShake[entity].prePosition.x
        entity.position.y = CShake[entity].prePosition.y
        bots.forEach { CPosition[it].load() }

        val xShake = MathUtils.random(-2f,2f)
        val yShake = MathUtils.random(-2f,2f)

        entity.position.x+=xShake
        entity.position.y+=yShake
        bots.forEach { CPosition[it].position.add(xShake,yShake) }

    }
}

class CShake : Component{
    var dirty = true
    var prePosition = Vector2()
    companion object : ComponentResolver<CShake>(CShake::class.java)
}
