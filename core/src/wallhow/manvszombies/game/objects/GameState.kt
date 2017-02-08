package wallhow.manvszombies.game.objects

object GameState {
    var points: Int = 0 // Очки опыта
    var greenKilled = 0
    var blueKilled = 0
    var redKilled = 0


    /**
     * Характеристики уровня
     */
    var level : Int = 1
        private set
    var botsGreen: Int = 0
        private set
    var botsBlue: Int = 0
        private set
    var botsRed: Int = 0
        private set
    var botsCount: Int = 0

    fun init() {
        val d = 0.9f * level
        botsBlue = (0 + d).toInt()
        botsGreen = (10 + d).toInt()
        botsRed = (1 + d).toInt()
        botsCount = botsBlue + botsGreen + botsRed
    }
    fun levelUp() {
        level++
        init()
    }

    val newWave: Boolean
        get() = botsCount<=0

}

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

        GunV1(MobHpV1 /2,0f),
        GunV2(MobHpV2 /3,0f),
        GunV3(MobHpV3 /4,0f)
    }
}