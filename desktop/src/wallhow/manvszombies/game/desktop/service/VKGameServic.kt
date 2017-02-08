package wallhow.manvszombies.game.desktop.service

/**
 * Created by wallhow on 06.02.17.
 */

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Array
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import wallhow.acentauri.utils.social.GameService
import javax.xml.ws.http.HTTPException

/**
 * Created by wallhow on 02.02.17.
 */
class VKGameService : GameService {
    lateinit var vk: VkApiClient
    val app_id: Int = 5829473
    val client_secret = "DJFH4759KFH5Fvf"

    override fun authorize(code : String): Boolean {
        val pref = Gdx.app.getPreferences("vk_gameService")

        try {
            val transportClient = HttpTransportClient.getInstance()
            vk = VkApiClient(transportClient)
            //val authResponse = vk.oauth().serviceClientCredentialsFlow(app_id, client_secret).execute()

            if (pref.getToken() == "") {
                val auth = vk.oauth().userAuthorizationCodeFlow(app_id, client_secret, "", code).execute()
                pref.setToken(auth.accessToken)
                pref.setUserId(auth.userId)

                println(pref.getToken())
                println(pref.getUserId())
            } else {
                val userActor = UserActor(pref.getUserId(), pref.getToken())
                println(vk.friends().get(userActor).execute().count)
            }
            return true
        }
        catch(e: HTTPException) {
            return false
        }
    }

    override fun logout() {

    }

    override fun setLevel(level: Int) {

    }

    override fun getLevel(): Int {
        val userActor = getUserActor()

        return vk.apps().getScore(userActor,userActor.id).execute()
    }

    override fun getFriends(): Array<GameService.User> {
        val userActor = getUserActor()
        val friends = Array<GameService.User>()
        vk.friends().getAppUsers(userActor).execute().forEach {
            val user = GameService.User(it.toString())
            val info = vk.users().get(userActor).userIds("${it}").execute()[0]
            user.firstName = info.firstName
            user.lastName = info.lastName
            user.level = vk.apps().getScore(userActor,app_id).execute()
            friends.add(user)
        }
        return friends
    }

    private fun getUserActor() : UserActor {
        val pref = Gdx.app.getPreferences("vk_gameService")
        return UserActor(pref.getUserId(), pref.getToken())
    }
}

fun Preferences.getUserId() : Int {
    return this.getInteger("userId")
}
fun Preferences.getToken() : String {
    return this.getString("t0k3n")
}
fun Preferences.setToken(arg : String) {
    this.putString("t0k3n",arg)
}
fun Preferences.setUserId(uid: Int) {
    this.putInteger("userId",uid)
}