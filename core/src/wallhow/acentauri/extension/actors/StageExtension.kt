package wallhow.acentauri.extension.actors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array

/**
 * Created by wallhow on 07.03.17.
 */
fun Stage.addActors(actors: Array<out Actor>) {
    for(i in 0..actors.size-1) {
        this.addActor(actors[i])
    }
}