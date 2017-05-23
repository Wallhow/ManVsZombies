package wallhow.acentauri.ashley.action

import wallhow.acentauri.ashley.action.actions.PositionX
import wallhow.acentauri.ashley.action.actions.PositionY
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.action.actions.Scale
import wallhow.acentauri.ashley.components.CPosition

/**
 * Created by wallhow on 23.05.17.
 */

fun ActionSequence.scaleTo(img: CImage,scaleTo: Float,time: Float,repeat : Boolean = false) : Scale {
    val scale = Scale(time,scaleTo,img,repeat)
    addAction(scale)
    return scale
}
fun ActionSequence.scaleTo(scale: Scale) {
    addAction(scale)
}

fun ActionSequence.xPositionTo(cPosition: CPosition, xTo : Float,time: Float, repeat: Boolean = false) : PositionX {
    val posX = PositionX(time,xTo,cPosition,repeat)
    addAction(posX)
    return posX
}
fun ActionSequence.yPositionTo(cPosition: CPosition, yTo : Float,time: Float, repeat: Boolean = false) : PositionY {
    val posY = PositionY(time,yTo,cPosition,repeat)
    addAction(posY)
    return posY
}