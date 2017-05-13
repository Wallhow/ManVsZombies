package wallhow.manvszombies.game.objects.models.weapons.abstracts

/**
 * Created by wallhow on 13.05.17.
 */
fun buildCharacteristic(timeReload: Float,maxCharge: Int,speedCharge:Float,fireRet: Float,damage: Float) : Weapon.Characteristic {
    return object : Weapon.Characteristic {
        override var timeReload: Float
            get() = timeReload
            set(value) {}
        override var maxCharge: Int
            get() = maxCharge
            set(value) {}
        override var speedCharge: Float
            get() = speedCharge
            set(value) {}
        override var fireRet: Float
            get() = fireRet
            set(value) {}
        override var damage: Float
            get() = damage
            set(value) {}
    }
}