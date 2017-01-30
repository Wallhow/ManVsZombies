package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import wallhow.acentauri.ashley.components.interfaces.ControllerListener

class CPlayerController(val controllerListener: ControllerListener) : Component {
    companion object : ComponentResolver<CPlayerController>(CPlayerController::class.java)
}

