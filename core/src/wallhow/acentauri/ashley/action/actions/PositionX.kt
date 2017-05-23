package wallhow.acentauri.ashley.action.actions

import wallhow.acentauri.ashley.action.DefAction
import wallhow.acentauri.ashley.action.Parameter
import wallhow.acentauri.ashley.components.CPosition

/**
 * Created by wallhow on 23.05.17.
 */
class PositionX (time: Float, xTo: Float, val cpos: CPosition, repeat: Boolean) :
        DefAction(time, Parameter(cpos.position.x,xTo)) {
    init {
        this.repeat = repeat
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(run) {
            cpos.position.x = currentData
        }
    }
}