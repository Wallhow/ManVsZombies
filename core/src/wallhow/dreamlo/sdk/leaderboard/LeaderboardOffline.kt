package wallhow.dreamlo.sdk.leaderboard

import com.badlogic.gdx.Gdx
import com.google.gson.reflect.TypeToken
import wallhow.dreamlo.sdk.DreamloSDK
import wallhow.dreamlo.sdk.utils.User
import java.util.*

/**
 * Created by wallhow on 24.02.17.
 */
class LeaderboardOffline : ILeaderboard {
    private val type = object : TypeToken<HashMap<String, User>>(){}.type
    private val users: HashMap<String,User>
    private val userNotFound = User().apply { name = User.NOT_FOUND; score = -1 }

    init {
        val dreamloPref = Gdx.app.getPreferences(DreamloSDK.LEADERBOARD_STORAGE_NAME)
        var json = ""
        if(dreamloPref.getString(DreamloSDK.LEADERBOARD_RECORDS_KEY,"")!="") {
            json = dreamloPref.getString(DreamloSDK.LEADERBOARD_RECORDS_KEY)
        }
        else {
            println("no rec")
            val map = HashMap<String,User>()
            map.put("Not connect", userNotFound)
            val jsonDef = DreamloSDK.gson.toJson(map)
            dreamloPref?.putString(DreamloSDK.LEADERBOARD_RECORDS_KEY,jsonDef)
            json = jsonDef
        }

        users = fromJson(json)
    }

    override fun setScore(name: String, score: Int) {
       //if(users[name] != null)
        //   users[name]?.score = score
        //else {
        //   users.put(name,User().apply { this.score = score; this.name = name })
       //}
    }

    override fun setUser(user: User) {
        //users.put(user.name,user)
    }

    override fun getAllUsers(): Array<User>? {
        return users.values.toTypedArray()
    }

    override fun getTopUsers(top: Int): Array<User> {
        val sorted = users.values.sorted()
        if(sorted.size <= top )
            return sorted.subList(0,sorted.size-1).toTypedArray()
        else
            return sorted.subList(0,top).toTypedArray()
    }

    override fun getUsers(firstIndex: Int, countUser: Int): Array<User> {
        val sorted = users.values.sorted()
        return sorted.subList(firstIndex,firstIndex+countUser-1).toTypedArray()
    }

    override fun getUser(name: String): User {
        return users[name] ?: userNotFound
    }

    override fun getScore(name: String): Int {
       return getUser(name).score
    }

    override fun fromJson(json: String): HashMap<String, User> {
        return DreamloSDK.gson.fromJson(json,type)
    }

    override fun getJson(): String {
        return DreamloSDK.gson.toJson(users)
    }

    override fun saveInLocalStorage() {

    }
}