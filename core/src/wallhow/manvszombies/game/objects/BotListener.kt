package wallhow.manvszombies.game.objects

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import wallhow.acentauri.ashley.components.extension.tryGet
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CKick
import wallhow.acentauri.ashley.components.CDelete
import wallhow.manvszombies.game.objects.models.Bot
import wallhow.manvszombies.game.objects.models.TypeZombie
import wallhow.manvszombies.game.objects.models.Zombie

//TODO Организовать получение данных об убитых мобах. Добавить очки и т.д.
// Слушатель смерти мобов, Нужен для контроля над очками, количеством мобов и т.д.
class BotListener : Listener<Bot> , EntityListener {
    init {
        Game.engine.addEntityListener(this)
    }
    override fun entityRemoved(entity: Entity) {
        entity.tryGet(CKick)?.let {
            GameState.botsCount--
            println("bots = ${GameState.botsCount}")
        }
    }
    override fun entityAdded(entity: Entity?) {

    }

    override fun receive(signal: Signal<Bot>?, bot: Bot) {
        bot.add(CDelete())
        val records = Game.injector.getInstance(GameRecords::class.java)
        when((bot as Zombie).type) {
            TypeZombie.GREEN -> { GameState.points += 100; GameState.greenKilled++; records.killBot(TypeZombie.GREEN)}
            TypeZombie.BLUE -> { GameState.points += 30; GameState.blueKilled++; records.killBot(TypeZombie.BLUE) }
            TypeZombie.RED -> { GameState.points += 120; GameState.redKilled++; records.killBot(TypeZombie.RED) }

        }


        println("points = ${GameState.points}")
    }
}

