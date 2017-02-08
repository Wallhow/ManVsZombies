package wallhow.acentauri.utils

import com.badlogic.gdx.utils.Timer

/**
 * Created by wallhow on 03.02.2017.
 */

inline fun gdxSchedule(delaySeconds: Float, intervalSeconds: Float, repeatCount: Int, crossinline run :() -> Unit) {
    Timer.schedule(object : Timer.Task() {
        override fun run() {
            run.invoke()
        }
    },delaySeconds,intervalSeconds,repeatCount)
}

inline fun gdxSchedule(delaySeconds: Float,intervalSeconds: Float,crossinline run :() -> Unit) {
    Timer.schedule(object : Timer.Task() {
        override fun run() {
            run.invoke()
        }
    },delaySeconds,intervalSeconds)
}

inline fun gdxSchedule(delaySeconds: Float,crossinline run :() -> Unit) {
    Timer.schedule(object : Timer.Task() {
        override fun run() {
            run.invoke()
        }
    },delaySeconds)
}
inline fun gdxPostTask(crossinline run: () -> Unit) {
    Timer.instance().postTask(object : Timer.Task() {
        override fun run() {
            run.invoke()
        }
    })
}
