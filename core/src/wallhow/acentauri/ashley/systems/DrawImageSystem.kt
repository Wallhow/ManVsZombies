package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.*
import wallhow.acentauri.ashley.components.extension.position
import wallhow.acentauri.ashley.components.extension.zIndex
import java.util.*

/**
 * Created by wallhow on 09.12.16.
 */
class DrawImageSystem @Inject constructor(val batch: SpriteBatch,val camera: OrthographicCamera): SortedIteratingSystem(Family.all(CPosition::class.java, CImage::class.java)
        .exclude(CInvisible::class.java).get(), Comparator { e1: Entity, e2: Entity ->
    if (e1.zIndex < e2.zIndex) -1
    else if(e1.zIndex > e2.zIndex) 1
    else 0
}
        ) {
    /**
     * Comparator { e1: Entity, e2: Entity ->
    if (CPosition[e1].position.y < CPosition[e2].position.y) -1
    else 0
    }
     */
    init {

    }


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val pos = entity.position
        val it = CImage[entity]

        if (it.isPlay()) {
            it.time += deltaTime
            it.nextFrame()
        }
        batch.color = it.color
        batch.draw(it.getFrame(), pos.x, pos.y,
                (it.width * 0.5f), (it.height * 0.5f),
                it.width, it.height,
                it.scale, it.scale,
                it.rotation)
        batch.color = Color.WHITE

    }

    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }
}


