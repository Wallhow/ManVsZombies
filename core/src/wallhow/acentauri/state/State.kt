package wallhow.acentauri.state

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wallhow.acentauri.state.StateManager
import java.util.*

/**
 * Created by wallhow on 09.01.17.
 */
interface State {
    val name:String
    fun initialize()
    fun load(context: State)
    fun render(sb: SpriteBatch)
    fun update(delta: Float)
    fun event(event: StateManager.Event)
    fun dispose()
    fun getInputProcessListener() : InputProcessor?
}
