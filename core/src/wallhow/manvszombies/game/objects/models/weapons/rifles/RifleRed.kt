package wallhow.manvszombies.game.objects.models.weapons.rifles

import com.badlogic.gdx.graphics.Color
import wallhow.manvszombies.game.objects.models.Player
import wallhow.manvszombies.game.objects.models.gun.GunType
import wallhow.manvszombies.game.objects.models.weapons.abstracts.Weapon

/**
 * Created by wallhow on 13.05.17.
 */
class RifleRed (player: Player) : Rifle(player) {
    init {
        characteristic.timeReload = 1.5f
        characteristic.fireRet = 1f
        characteristic.speedCharge*=0.5f
        bulletColor = Color.RED
        gunType = GunType.RED
        bulletScale = 2f
    }
}