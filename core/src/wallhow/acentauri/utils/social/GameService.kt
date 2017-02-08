package wallhow.acentauri.utils.social

import com.badlogic.gdx.utils.Array

/**
 * Created by wallhow on 01.02.2017.
 */
interface GameService {
    fun authorize(code : String) : Boolean
    fun logout()

    fun setLevel(level : Int)
    fun getLevel() : Int
    fun getFriends() : Array<User>


    data class User(val uid: String) {
        var firstName = ""
        var lastName = ""
        var city = ""
        var level = 0
        var score = 0
        var other = null
    }
}