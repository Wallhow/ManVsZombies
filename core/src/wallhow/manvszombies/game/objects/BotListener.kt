package wallhow.manvszombies.game.objects

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.models.Bot
import wallhow.manvszombies.game.objects.models.TypeZombie
import wallhow.manvszombies.game.objects.models.Zombie

//TODO Организовать получение данных об убитых мобах. Добавить очки и т.д.
// Слушатель смерти мобов, Нужен для контроля над очками, количеством мобов и т.д.
class BotListener : Listener<Bot> {
    override fun receive(signal: Signal<Bot>?, bot: Bot) {
        GameState.botsCount--

        when((bot as Zombie).type) {
            TypeZombie.GREEN -> { GameState.points += 100; GameState.greenKilled++ }
            TypeZombie.BLUE -> { GameState.points += 30; GameState.blueKilled++ }
            TypeZombie.RED -> { GameState.points += 120; GameState.redKilled++ }
        }


        println("points = ${GameState.points}")
    }
}

