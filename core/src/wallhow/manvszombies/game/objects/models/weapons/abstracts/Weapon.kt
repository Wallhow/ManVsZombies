package wallhow.manvszombies.game.objects.models.weapons.abstracts

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import wallhow.acentauri.ashley.components.ComponentResolver

/**
 * Created by wallhow on 13.05.17.
 */
interface Weapon : Component {
    companion object : ComponentResolver<Weapon>(Weapon::class.java)
    fun shot(direction: Vector2)
    fun reload()
    var isReload : Boolean
    var currentCharge : Int
    fun characteristic() : Characteristic

    data class Characteristic(var timeReload : Float,
                              var maxCharge : Int,
                              var speedCharge : Float,
                              var fireRet : Float ,
                              var damage: Float  )//Темп стрельбы
}
