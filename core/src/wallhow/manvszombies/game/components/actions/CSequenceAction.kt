package wallhow.manvszombies.game.components.actions

import com.badlogic.ashley.core.Entity
import wallhow.acentauri.ashley.components.ComponentResolver

/**
 * Created by wallhow on 22.01.17.
 */
class CSequenceAction (var repeat : Int,val finaleAction: CAction?,vararg actions:CAction) : CAction {
    companion object : ComponentResolver<CSequenceAction>(CSequenceAction::class.java)
    override var currentTime = 0f
    override var ready = false
    private var index = 0
    private val array: Array<out CAction>
    init {
        array = actions
    }

    override fun update(entity: Entity, delta: Float) {
        if (index >= array.size && repeat == 0) {
            finaleAction?.let { it.update(entity, delta) }
        } else {
            if(index >= array.size) {
                index=0
            }
            array[index].update(entity, delta)

            if (array[index].ready) {
                if (repeat < 0 || repeat > 0) {
                    array[index].currentTime = 0f
                    array[index].ready = false
                }
                index++
                repeat--
            }
        }
    }
}