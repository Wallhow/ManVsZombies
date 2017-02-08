package wallhow.manvszombies.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import wallhow.acentauri.ashley.components.ComponentResolver

class Task(val time: Float,var repeat: Int, val run : (entity : Entity) -> Unit) : Component {
    companion object : ComponentResolver<Task>(Task::class.java)
    var currentTime = 0f

    val ready : Boolean
        get() = currentTime>=time
}

fun Entity.task(time: Float, run: (entity: Entity) -> Unit) {
    this.add(Task(time,1,run))
}
fun Entity.repeatTask(time: Float, repeat: Int,run: (entity: Entity) -> Unit) {
    this.add(Task(time,repeat,run))
}
fun Entity.endlessTask(time: Float,run: (entity: Entity) -> Unit) {
    this.add(Task(time,-1,run))
}