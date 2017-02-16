package wallhow.manvszombies.game.objects

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.DeleteMe
import wallhow.manvszombies.game.objects.models.Bot

/**
 * Created by wallhow on 16.02.17.
 */
class CellListener : Listener<Cell> {
    override fun receive(signal: Signal<Cell>, cell: Cell) {
        println("bot in cell = ${cell.objects.size}")
        for (bot in cell.objects) {
            bot as Bot
            Game.getDeadMobSignaler().dispatch(bot)
        }
        cell.objects.clear()
        Game.gameField.removeCell(cell)
        cell.add(DeleteMe())
    }
}