package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import wallhow.manvszombies.game.objects.models.Bot

/**
 * Created by wallhow on 06.02.17.
 */
class ExpSystem {

    init {
        val signal = Signal<Bot>()

        signal.add(MobListener())

        signal.dispatch(null)
    }

    class MobListener : Listener<Bot> {
        override fun receive(signal: Signal<Bot>?, e: Bot) {

        }
    }

}