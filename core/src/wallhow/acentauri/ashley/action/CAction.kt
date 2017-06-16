package wallhow.acentauri.ashley.action

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.Array
import wallhow.acentauri.ashley.components.ComponentResolver

/**
 * Created by wallhow on 23.05.17.
 */
data class CAction(var repeat : Boolean) : Component {
    companion object : ComponentResolver<CAction>(CAction::class.java)
    val sequence: ActionSequence = ActionSequence()
}

class ActionSequence {
    private val sequence: Array<IAction> = Array()
    private var iterator : Int = 0
    private var ready : Boolean = false
    fun addAction(action: IAction) {
        sequence.add(action)
    }
    fun getIterator() : Int = iterator
    fun nextSequence() {
        if(!ready) {
            iterator++
            if (iterator >= sequence.size) {
                ready = true
            }
        }
    }
    fun restart() {
        ready = false
        iterator = 0
    }
    fun getAction() : IAction = sequence[iterator]

    interface IAction {
        fun act(delta: Float)
        fun isReady() : Boolean
        fun isRepeat() : Boolean
        fun start()
        fun pause()
        fun stop()
        fun restart()
        fun restart(start: Float,end: Float)
    }
}

data class Parameter(var start : Float, var end: Float)

abstract class DefAction(var time : Float,parameter : Parameter) : ActionSequence.IAction {
    protected var interpolation : Interpolation = Interpolation.linear
    protected var ready = false
    protected var run = false
    protected var repeat = false
    protected val param = parameter
    protected var currentTime = 0f
    protected var currentData = param.start

    override fun act(delta: Float) {
        if(run) {
            currentTime += delta
            if (!ready) {
                val cTime = (currentTime / time)
                currentData = interpolation.apply(param.start, param.end, cTime)
                if (currentTime >= time ) {
                    if(repeat) {
                        restart()
                    }
                    else
                        ready = true
                }
            }
        }

    }

    override fun isReady() = ready
    override fun isRepeat() = repeat

    override fun start() {
        run = true
    }
    override fun pause() {
        run = false
    }

    override fun stop() {
        run = false
        restart()
    }

    override fun restart() {
        restart(param.start,param.end)
    }

    override fun restart(start: Float, end: Float) {
        if(param.start != start || param.end != end) {
            param.start = start
            param.end = end
        }
        ready = false
        currentTime = 0f
        currentData = param.start
    }
}