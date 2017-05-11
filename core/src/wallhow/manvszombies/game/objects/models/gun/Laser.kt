package wallhow.manvszombies.game.objects.models.gun

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

/**
 * Created by wallhow on 11.05.17.
 */
class Laser : Entity() {
    init {
        add(CGun())
    }
    fun shot(direction : Vector2) {

    }
}