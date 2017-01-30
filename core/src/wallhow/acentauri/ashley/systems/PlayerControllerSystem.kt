package wallhow.acentauri.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.*
import wallhow.acentauri.ashley.components.extension.controllerListener

class PlayerControllerSystem @Inject constructor(multiplexer: InputMultiplexer) : IteratingSystem(Family.all(CPlayerController::class.java).get()) {
    var keyPress = 0
    var pressed = false
    private var dirty: Boolean = true

    init {
        multiplexer.addProcessor( object : InputAdapter() {
            init {

            }
            override fun keyDown(keycode: Int): Boolean {

                if(keyPress != keycode && dirty) {
                    pressed = true
                    keyPress = keycode
                    dirty = false

                }
                return false
            }

            override fun keyUp(keycode: Int): Boolean {
               if(keyPress == keycode) {
                   keyPress = 0
                   pressed = false
                   dirty = false
               }
                else {

               }
                return false
            }
        })
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val controller = entity.controllerListener

        if( !dirty ) {

            when (keyPress) {
                Input.Keys.W -> {controller.up(); dirty = true}
                Input.Keys.S -> {controller.down(); dirty = true}
                Input.Keys.A -> {controller.left(); dirty = true}
                Input.Keys.D -> {controller.right(); dirty = true}
                0 -> {controller.none(); dirty = true }
            }
        }

    }


}