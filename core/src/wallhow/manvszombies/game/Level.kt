package wallhow.manvszombies.game

/**
 * Created by zhokin on 06.05.2016.
 */
class Level {

    var levelNumber: Int = 0
        private set
    var mobsCountGreen: Int = 0
    var mobsCountBlue: Int = 0
    var mobsCountRed: Int = 0
    var mobsCount = 0

    init {
        levelNumber = 1
        init()
    }

    fun init() {
        val d = 0.9f * levelNumber
        mobsCountBlue = (0 + d).toInt()
        mobsCountGreen = (10 + d).toInt()
        mobsCountRed = (1 + d).toInt()
        mobsCount = mobsCountBlue + mobsCountGreen + mobsCountRed
        println("mobCount:"+mobsCount)
    }

    fun reset() {
        levelNumber = 0
    }

    fun levelUp() {
        levelNumber++
        init()
    }
}