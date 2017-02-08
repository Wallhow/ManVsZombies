package wallhow.manvszombies.game.objects.models.gun

import wallhow.manvszombies.game.objects.Balance
import wallhow.manvszombies.game.objects.models.TypeZombie

enum class GunType(var timeReload: Float, var force: Float, val mobType: TypeZombie) {
    GREEN(1f, Balance.Effect.GunV2.force.toFloat(), TypeZombie.GREEN),
    BLUE(1f, Balance.Effect.GunV1.force.toFloat(), TypeZombie.BLUE),
    RED(1.5f, Balance.Effect.GunV3.force.toFloat(), TypeZombie.RED)
}