package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.*
import wallhow.acentauri.ashley.components.extension.movementComponent
import wallhow.acentauri.ashley.components.extension.position

class CollideDetectedSystem @Inject constructor() : EntitySystem() {
    override fun update(deltaTime: Float) {
        val array = engine.getEntitiesFor(Family.all(CPosition::class.java, CollideBoxComponent::class.java, CMovement::class.java)
                .get()).toArray()

        for (i1 in 0..array.size - 1) {
            if (i1 + 1 >= array.size) {
                return
            }
            val pos1 = array[i1].position
            val width1 = CollideBoxComponent[array[i1]].widthBox
            val height1 = CollideBoxComponent[array[i1]].heightBox
            val r1 = Rectangle(pos1.x, pos1.y, width1, height1)
            val vel = array[i1].movementComponent.velocity

            if (vel.x != 0f || vel.y != 0f) {
                for (i2 in i1 + 1..array.size - 1) {

                    val pos2 = array[i2].position
                    val width2 = CollideBoxComponent[array[i2]].widthBox
                    val height2 = CollideBoxComponent[array[i2]].heightBox
                    val r2 = Rectangle(pos2.x, pos2.y, width2, height2)
                    val intersection = Rectangle()


                    if (Intersector.intersectRectangles(r1, r2, intersection)) {
                        if (CollideBoxComponent[array[i2]].passable == 0) {
                            CollideBoxComponent[array[i1]].collideListener.beginCollide(array[i2])
                            CollideBoxComponent[array[i2]].collideListener.beginCollide(array[i1])
                        } else {
                            CollideBoxComponent[array[i1]].collideListener.beginCollide(array[i2])
                            CollideBoxComponent[array[i2]].collideListener.beginCollide(array[i1])

                            if (vel.x > 0 && intersection.height >= intersection.width) {
                                array[i1].position.x -= intersection.width
                                vel.x = 0f
                            } else
                                if (vel.x < 0 && intersection.height >= intersection.width) {
                                    array[i1].position.x += intersection.width
                                    vel.x = 0f
                                }
                            if (vel.y > 0 && intersection.width >= intersection.height) {
                                array[i1].position.y -= intersection.height
                                vel.y = 0f
                            } else
                                if (vel.y < 0 && intersection.width >= intersection.height) {
                                    array[i1].position.y += intersection.height
                                    vel.y = 0f
                                }

                            CollideBoxComponent[array[i1]].collideListener.endCollide(array[i2])
                            CollideBoxComponent[array[i2]].collideListener.endCollide(array[i1])
                        }
                    }
                }

            }

        }

    }
}