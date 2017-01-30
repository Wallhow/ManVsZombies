package wallhow.acentauri.process

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wallhow.acentauri.process.ProcessManager
import java.util.*

/**
 * Created by wallhow on 09.01.17.
 */
interface IProcess {
    val name:String
    fun initialize(userInfo: Any)
    fun load()
    fun render(sb: SpriteBatch)
    fun update(delta: Float)
    fun event(event: ProcessManager.EventProcess)
    fun dispose()
    fun getInputProcessListener() : InputProcessor?
}
