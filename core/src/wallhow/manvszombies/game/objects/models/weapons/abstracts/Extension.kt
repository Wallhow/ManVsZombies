package wallhow.manvszombies.game.objects.models.weapons.abstracts

/**
 * Created by wallhow on 13.05.17.
 */
fun buildCharacteristic(timeReload: Float,maxCharge: Int,speedCharge:Float,fireRet: Float,damage: Float) : Weapon.Characteristic {
    return Weapon.Characteristic(timeReload,maxCharge,speedCharge,fireRet,damage)
}