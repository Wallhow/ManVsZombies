package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.acentauri.ashley.components.extension.*
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CHealth
import wallhow.manvszombies.game.components.CKickMob
import wallhow.manvszombies.game.components.DeleteMe
import wallhow.manvszombies.game.components.actions.CColorAction
import wallhow.manvszombies.game.components.actions.CDeletAction
import wallhow.manvszombies.game.components.actions.CSequenceAction
import wallhow.manvszombies.game.objects.*
import wallhow.manvszombies.game.objects.models.*
import wallhow.manvszombies.game.objects.models.gun.GunType

/**
 * Created by wallhow on 22.01.17.
 */
class KickMobSystem @Inject constructor() : IteratingSystem(Family.all(CKickMob::class.java).get()){
    override fun processEntity(entity: Entity, deltaTime: Float) {
        if(entity.position.x < -20 || entity.position.y < 0 ||
                entity.position.x>Game.viewport.worldWidth || entity.position.y>Game.viewport.worldHeight+20) {
            entity.add(DeleteMe())
            return
        }

        val kick = CKickMob[entity]
        if(kick.gunType.equals(GunType.GREEN)) {
            checkCollide(engine.getEntitiesFor(Family.all(ZombieGreen::class.java).get()),entity)
        } else if(kick.gunType.equals(GunType.BLUE)) {
            checkCollide(engine.getEntitiesFor(Family.all(ZombieBlue::class.java).get()),entity)
        }else if(kick.gunType.equals(GunType.RED)) {
            checkCollide(engine.getEntitiesFor(Family.all(ZombieRed::class.java).get()),entity)
        }


    }
    private fun checkCollide(array: ImmutableArray<Entity>,entity: Entity) {
        array.forEach {
            val mob = Rectangle(it.position.x,it.position.y,it.width,it.height)
            val bullet = Rectangle(entity.position.x,entity.position.y,entity.width,entity.height)

            if(bullet.overlaps(mob)) {
                CHealth[it].currentHealth-=CKickMob[entity].gunType.force
                if(CHealth[it].currentHealth<=0) {
                    val rip = Entity()
                    rip.add(CPosition[it])
                    rip.add(CImage(Game.atlas.findRegion("rip"),31f,13f,31f,13f).apply {
                        val r =MathUtils.random(0,1)

                        color = it.color.cpy()
                        scale = it.scale
                        if(r==1) {
                            nextFrame()
                            getFrame().flip(true, false)
                        }
                    })
                    rip.add(CSequenceAction(8, CDeletAction(1f),CColorAction(0.5f, Color.WHITE.cpy()),CColorAction(0.5f, rip.color)))
                    engine.addEntity(rip)

                    //Убираем моба из общего количества
                    // Обробатываем его убийство
                    Game.getDeadMobSignaler().dispatch(it as Bot)
                }
                entity.add(DeleteMe())
            }
        }
    }
}
