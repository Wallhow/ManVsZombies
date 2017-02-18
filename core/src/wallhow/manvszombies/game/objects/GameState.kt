package wallhow.manvszombies.game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import wallhow.acentauri.ashley.components.extension.zIndex

object GameState {
    var points: Int = 0 // Очки опыта
    var greenKilled = 0
    var blueKilled = 0
    var redKilled = 0

    var nickName = "player"


    /**
     * Характеристики уровня
     */
    var level : Int = 20
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


    fun reset() {
        points = 0
        greenKilled = 0
        redKilled = 0
        blueKilled = 0
        level = 1
        botsCount = 0
    }
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
        MobV1(5,2.5f),
        MobV2(20,4f),
        MobV3(70,7f),

        GunV1(MobHpV1 /2,0f),
        GunV2(MobHpV2 /3,0f),
        GunV3(MobHpV3 /4,0f)
    }
}

class GameRecords {
    private val preferences: Preferences
    private var records : Array<Record>
    init  {
        preferences = Gdx.app.getPreferences("localRecords")

        if(preferences.getString("records")=="") {
            val records1 = Array<Record>()
            records1.add(record("Bob",3))
            records1.add(record("Mike",5))
            records1.add(record("Sem",2))
            records1.add(record("Dr.Who",10))
            sortRecord(records1)

            val json = Json()
            val jsonString = json.toJson(records1)
            preferences.putString("records",jsonString).flush()
        }
        records = Array<Record>()
        records = Json().fromJson(records.javaClass,preferences.getString("records"))
        sortRecord(records)

    }
    fun record(name: String,wave: Int) : Record {
        return Record().apply { namePlayer = name; this.wave = wave }
    }

    /**
     * @param Массив рекордов для сортировки, рекорды сортируются - наивысший : ниже в массиве
     */
    fun sortRecord(records : Array<Record>) {
        records.sort { record, record1 -> if (record.wave < record1.wave) 1
        else if(record.wave > record1.wave) -1
        else 0}
    }

    class Record {
        var namePlayer: String = ""
        var wave: Int = 0
    }

    fun get() : Array<Record> {
        return records
    }

}