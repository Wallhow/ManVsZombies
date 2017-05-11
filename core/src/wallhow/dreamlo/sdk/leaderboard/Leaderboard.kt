package wallhow.dreamlo.sdk.leaderboard

import com.badlogic.gdx.Gdx
import com.google.gson.reflect.TypeToken
import wallhow.dreamlo.sdk.DreamloSDK
import wallhow.dreamlo.sdk.utils.User
import java.util.*

/**
 * Created by wallhow on 23.02.17.
 */
class Leaderboard : ILeaderboard {
    private val mapUsers =  ArrayList<Pair<String, User>>()
    private val userNotFound = User().apply { name = User.NOT_FOUND }

    var update = true

    override fun setScore(name: String, score: Int) {
        DreamloSDK.privateExecute("add/$name/$score")
        update = true
    }
    override fun setUser(user: User) {
        DreamloSDK.privateExecute("add/${user.name}/${user.score}/${user.seconds}/${user.text}")
        update = true
    }

    override fun getAllUsers() : Array<User>? {
        if(update) {
            updateUserBase()
        }
        return mapUsers.map { it.second }.toTypedArray()
    }
    override fun getTopUsers(top: Int) : Array<User> {
        val array = DreamloSDK.publicExecute(("pipe-get/")).split("\n").filter { it!="" }
        val top1 = if(top>array.size-1) array.size-1 else top
        val usersTop = Array(top1-1) { User.create(array[it],"|") }
        return usersTop
    }
    override fun getUsers(firstIndex : Int, countUser : Int) : Array<User> {
        val array = DreamloSDK.publicExecute(("pipe-get/$firstIndex/${countUser-1}")).split("\n").filter { it!="" }
        return array.map { User.create(it, "|") }.toTypedArray()
    }
    override fun getUser(name: String) : User {
        if(update) updateUserBase()
        return mapUsers.firstOrNull { it.first.contains(name) }?.second ?: userNotFound
    }

    override fun getScore(name: String) : Int = getUser(name).score

    private fun updateUserBase() {
        mapUsers.clear()
        val array = DreamloSDK.publicExecute(("pipe-get/")).split("\n").filter { it!="" }
        array.map {
            val usr=User.create(it, "|")
            mapUsers.add(Pair(usr.name,usr))
            usr }
        update = false
    }

    private val type = object : TypeToken<HashMap<String,User>>(){}.type
    override fun fromJson(json: String) : HashMap<String,User> {
        return DreamloSDK.gson.fromJson(json,type)
    }
    override fun getJson() : String {
        val hmap = HashMap<String,User>()
        getAllUsers()?.forEach { hmap.put(it.name,it) }
        return DreamloSDK.gson.toJson(hmap)
    }

    override fun saveInLocalStorage() {
        Gdx.app.getPreferences(DreamloSDK.LEADERBOARD_STORAGE_NAME)
                .putString(DreamloSDK.LEADERBOARD_RECORDS_KEY,getJson()).flush()
    }
}