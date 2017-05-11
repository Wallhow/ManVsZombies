package wallhow.manvszombies.game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.google.gson.reflect.TypeToken

object GameState {
    var points: Int = 0 // Очки опыта
    var greenKilled = 0
    var blueKilled = 0
    var redKilled = 0

    var nickName = "player"


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


    fun reset() {
        points = 0
        greenKilled = 0
        redKilled = 0
        blueKilled = 0
        level = 1
        botsCount = 0
    }

    var isOnline = false
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
    private val preferences: Preferences = Gdx.app.getPreferences("localRecords")
    private var records : Array<Record>
    private val storageKey = "GameRecords"
    init  {

        if(preferences.getString(storageKey)=="") {
            val records1 = Array<Record>()
            records1.add(record("Bob",3))
            records1.add(record("Mike",5))
            records1.add(record("Sam",2))
            records1.add(record("Dr.Who",10))
            sortRecord(records1)

            val json = Json()
            val jsonString = json.toJson(records1)
            preferences.putString(storageKey,jsonString).flush()
        }
        records = Array<Record>()
        records = Json().fromJson(records.javaClass,preferences.getString(storageKey))
        sortRecord(records)

    }
    fun record(name: String,wave: Int) : Record {
        return Record().apply { namePlayer = name; this.wave = wave }
    }

    fun flush() {
        val json = Json()
        val jsonString = json.toJson(records)
        preferences.putString(storageKey,jsonString).flush()
        sortRecord(records)
        println("record flush")
    }

    /**
     * @param Масив рекордов для сортировки, рекорды сортируются - наивысший : ниже в массиве
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

class Achievements(val preferences: Preferences) {
    private var internals: kotlin.Array<Int>
    private val storageKey = "a4ievements"
    init {
        internals = kotlin.Array(Internal.values().size) {
            0
        }
    }

    fun get(type : Internal) : Int {
        return internals[type.ordinal]
    }
    fun set(type : Internal, flag: Int) {
        internals[type.ordinal] = flag
    }
    fun flush() {
        preferences.putString(storageKey,Json().toJson(internals))
    }
    fun load() {
        internals = Json().fromJson(internals.javaClass, preferences.getString(storageKey))
    }

    enum class Internal(val nameAchievement: String,val text: String) {
        Killed25Bots("Убить 25 зомби!","Текст ачивки"),
        Killed50Bots("Убить 50 ботов!","Текст ачивки"),
        Replay2("Азарт!","Переиграть игру"),
        Replay5("Войти во вкус.","Переиграть игру 5 раз"),
        Replay10("replay 10","Текст ачивки"),
        Test1("test","test"),
        Test2("test","test"),
        Test3("test","test"),
        Test4("test","test"),
        Test5("test","test"),
        Test6("test","test"),
    }
}