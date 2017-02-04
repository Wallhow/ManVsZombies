package wallhow.manvszombies.game.objects

/**
 * Created by wallhow on 04.02.2017.
 */
object Balance {
    val FieldHpV1 = 600
    val FieldHpV2 = 900
    val FieldHpV3 = 1200

    val MobHpV1 = 200
    val MobHpV2 = 300
    val MobHpV3 = 400

    val MobForceV1 = 10
    val MobForceV2 = 20
    val MobForceV3 = 50
    val MobForceTimeV1 = 1f

    enum class Effect(var force: Int, var timeForce: Float) {
        MobV1(10,2.5f),
        MobV2(20,3f),
        MobV3(50,5f),

        GunV1(MobHpV1/2,0f),
        GunV2(MobHpV2/3,0f),
        GunV3(MobHpV3/4,0f)
    }
}