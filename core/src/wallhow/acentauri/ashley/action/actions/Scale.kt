package wallhow.acentauri.ashley.action.actions

import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.action.DefAction
import wallhow.acentauri.ashley.action.Parameter

/**
 * Created by wallhow on 23.05.17.
 */
class Scale  (time: Float,scaleTo: Float,val img: CImage,repeat: Boolean) :
        DefAction(time, Parameter(img.scale,scaleTo)) {
    init {
        this.repeat = repeat
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(run) {
            img.scale = currentData
        }
    }
}