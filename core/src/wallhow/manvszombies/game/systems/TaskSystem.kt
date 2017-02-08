package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.models.gun.CGun
import wallhow.manvszombies.game.components.Task

/**
 * Created by wallhow on 06.02.17.
 */
class TaskSystem @Inject constructor() : IteratingSystem(Family.all(Task::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        Task[entity].currentTime+=deltaTime

        if(Task[entity].ready) {
            Task[entity].run(entity)
            if(Task[entity].repeat>=0) {
                Task[entity].currentTime = 0f
                Task[entity].repeat--
                if(Task[entity].repeat == 0)
                    entity.remove(Task::class.java)
            }
            else if(Task[entity].repeat<0) {
                Task[entity].currentTime = 0f
            }
        }

    }

    //TODO Сделать паузу у всех (кроме рисующих) Систем.
    override fun checkProcessing(): Boolean {
        return !Game.pause
    }
}