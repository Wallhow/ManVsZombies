package wallhow.acentauri.ashley.action.actions

import wallhow.acentauri.ashley.action.DefAction
import wallhow.acentauri.ashley.action.Parameter
import wallhow.acentauri.ashley.components.CPosition

/**
 * Created by wallhow on 23.05.17.
 */
class PositionY (time: Float, yTo: Float, val cpos: CPosition, repeat: Boolean) :
        DefAction(time, Parameter(cpos.position.y,yTo)) {
    init {
        this.repeat = repeat
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(run) {
            cpos.position.y = currentData
        }
    }
}