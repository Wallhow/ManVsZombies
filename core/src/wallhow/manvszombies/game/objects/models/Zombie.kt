package wallhow.manvszombies.game.objects.models

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.Viewport
import wallhow.acentauri.ashley.components.CImage
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.objects.GameTable
import wallhow.manvszombies.game.components.CHealth
import wallhow.manvszombies.game.components.CKick
import wallhow.manvszombies.game.objects.Balance

/**
 * Created by wallhow on 13.01.17.
 */
class Zombie (val type: TypeZombie, val cell: GameTable.Cell) : Bot() {
    init {
        val view = Game.injector.getInstance(Viewport::class.java)
        val r = MathUtils.random(0,1)
        when (type) {
            TypeZombie.GREEN -> {
                var sequence = intArrayOf(2, 0, 1, 0)
                if(r == 0)
                    sequence = intArrayOf(0,2,0,1)

                createImage(Game.atlas.findRegion("bot"),18f,30f,sequence)
                CImage[this].color = Color.GREEN
                CImage[this].scale = 1.3f
                val x = MathUtils.random(0f,view.worldWidth - CImage[this].width)
                val y = MathUtils.random(view.worldHeight,view.worldHeight + CImage[this].height*10)
                createPosition(Vector2(x,y))
                add(CHealth(Balance.MobHpV2.toFloat()).apply { draw = false })
                add(CKick(Balance.Effect.MobV2.force.toFloat(), Balance.Effect.MobV2.timeForce,cell))
                add(ZombieGreen())
                //createMovement(Vector2(0f,-1f),45f)
            }
            TypeZombie.BLUE -> {
                var sequence = intArrayOf(1, 2)
                if(r == 0)
                    sequence = intArrayOf(2,1)

                createImage(Game.atlas.findRegion("bot"),18f,30f,sequence)
                CImage[this].color = Color.VIOLET
                CImage[this].scale = 1.2f
                val x = MathUtils.random(0f,view.worldWidth - CImage[this].width)
                val y = MathUtils.random(view.worldHeight,view.worldHeight + CImage[this].height*20)
                createPosition(Vector2(x,y))
                add(CHealth(Balance.MobHpV1.toFloat()).apply { draw = false })
                add(CKick(Balance.Effect.MobV1.force.toFloat(), Balance.Effect.MobV1.timeForce,cell))
                add(ZombieBlue())
                //createMovement(Vector2(0f,-1f),55f)
            }
            TypeZombie.RED -> {
                var sequence = intArrayOf(2, 0, 1, 0)
                if(r == 0)
                    sequence = intArrayOf(0, 2, 0, 1)

                createImage(Game.atlas.findRegion("bot"),18f,30f,sequence)
                CImage[this].color = Color.RED
                CImage[this].scale = 1.8f
                val x = MathUtils.random(0f,view.worldWidth - CImage[this].width)
                val y = MathUtils.random(view.worldHeight + CImage[this].height*4,view.worldHeight + CImage[this].height*10)
                createPosition(Vector2(x,y))
                add(CHealth(Balance.MobHpV3.toFloat()).apply { draw = false })
                add(CKick(Balance.Effect.MobV3.force.toFloat(), Balance.Effect.MobV3.timeForce,cell))
                add(ZombieRed())
                //createMovement(Vector2(0f,-1f),30f)
            }
        }
        cell.objects.add(this)
        CImage[this].play()
    }
}

class ZombieGreen : Component
class ZombieRed : Component
class ZombieBlue : Component


enum class TypeZombie {
    GREEN,RED,BLUE,BOSS
}
