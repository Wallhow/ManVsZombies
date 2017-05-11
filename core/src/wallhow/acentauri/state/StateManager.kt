package wallhow.acentauri.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.GdxRuntimeException

/**
 * Created by wallhow on 12.01.17.
 */
class StateManager : InputAdapter() {
    private val processList = Array<State>()
    private var currentProcess: State

    init {
        currentProcess = StateAdapter("default")
    }

    fun addProcess(process: State, current : Boolean = false) {
        for(i in 0..processList.size-1) {
            if(processList[i].name == process.name) {
                return
            }
        }
        process.initialize()
        processList.add(process)
        if(current)
            setCurrentProcess(process.name)
    }

    fun removeProcess(process: State) {
        for(i in 0..processList.size-1) {
            if(processList[i].name == process.name) {
                processList.removeIndex(i)
                return
            }
        }
        throw GdxRuntimeException("state не найден")
    }

    fun setCurrentProcess(name: String) {
        for(i in 0..processList.size-1) {
            if(processList[i].name == name) {
                processList[i].load(currentProcess)
                currentProcess = processList[i]
                return
            }
        }
    }

    fun show() {
        currentProcess.load(currentProcess)
    }

    fun pause() {
        currentProcess.event(Event.Pause)
    }

    fun resize(width: Int, height: Int) {
        Event.Resize.data = Vector2(width.toFloat(),height.toFloat())
        currentProcess.event(Event.Resize)
    }

    fun hide() {
        currentProcess.event(Event.Hide)
    }

    fun render(spriteBatch: SpriteBatch) {
        currentProcess.update(Gdx.graphics.deltaTime)
        currentProcess.render(spriteBatch)
    }

    fun resume() {
        currentProcess.event(Event.Resume)
    }

    fun dispose() {
        processList.forEach(State::dispose)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        currentProcess.getInputProcessListener()?.touchDown(screenX,screenY,pointer,button)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        currentProcess.getInputProcessListener()?.touchUp(screenX,screenY,pointer,button)
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        currentProcess.getInputProcessListener()?.keyDown(keycode)
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        currentProcess.getInputProcessListener()?.keyUp(keycode)
        return false
    }

    enum class Event(var data: Any?) {
        Pause(null),Resize(Vector2()),Hide(null),Resume(null)
    }
    open class StateAdapter(override val name: String) : State {
        override fun initialize() {

        }

        override fun load(context: State) {
        }

        override fun render(sb: SpriteBatch) {
        }

        override fun update(delta: Float) {

        }

        override fun event(event: Event) {
        }

        override fun dispose() {
        }

        override fun getInputProcessListener(): InputProcessor? {
            return null
        }
    }
}