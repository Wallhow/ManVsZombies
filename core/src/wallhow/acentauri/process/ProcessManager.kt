package wallhow.acentauri.process

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
class ProcessManager : InputAdapter() {
    private val processList = Array<IProcess>()
    private var currentProcess: IProcess

    init {
        currentProcess = ProcessAdapter("default")
    }

    fun addProcess(process: IProcess, current : Boolean = false) {
        for(i in 0..processList.size-1) {
            if(processList[i].name == process.name) {
                return
            }
        }
        processList.add(process)
        if(current)
            setCurrentProcess(process.name)
    }

    fun removeProcess(process: IProcess) {
        for(i in 0..processList.size-1) {
            if(processList[i].name == process.name) {
                processList.removeIndex(i)
                return
            }
        }
        return throw GdxRuntimeException("process не найден")
    }

    fun setCurrentProcess(name: String) {
        val process = currentProcess.name
        for(i in 0..processList.size-1) {
            if(processList[i].name == name) {
                currentProcess = processList[i]
                currentProcess.initialize(process)
                currentProcess.load()
                return
            }
        }
    }

    fun show() {

    }

    fun pause() {
        currentProcess.event(EventProcess.Pause)
    }

    fun resize(width: Int, height: Int) {
        EventProcess.Resize.data = Vector2(width.toFloat(),height.toFloat())
        currentProcess.event(EventProcess.Resize)
    }

    fun hide() {
        currentProcess.event(EventProcess.Hide)
    }

    fun render(spriteBatch: SpriteBatch) {
        currentProcess.update(Gdx.graphics.deltaTime)
        currentProcess.render(spriteBatch)
    }

    fun resume() {
        currentProcess.event(EventProcess.Resume)
    }

    fun dispose() {
        processList.forEach(IProcess::dispose)
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

    enum class EventProcess(var data: Any?) {
        Pause(null),Resize(Vector2()),Hide(null),Resume(null)
    }
    open class ProcessAdapter(override val name: String) : IProcess {
        override fun initialize(userInfo: Any) {

        }

        override fun load() {
        }

        override fun render(sb: SpriteBatch) {
        }

        override fun update(delta: Float) {

        }

        override fun event(event: EventProcess) {
        }

        override fun dispose() {
        }

        override fun getInputProcessListener(): InputProcessor? {
            return null
        }
    }
}