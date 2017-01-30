package wallhow.vkdesktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.vk.api.sdk.actions.Secure
import wallhow.acentauri.vkapi.VKApi
import wallhow.acentauri.vkapi.VKUser
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.client.actors.UserActor
import wallhow.acentauri.vkapi.VKEvent


/**
 * Created by wallhow on 14.01.17.
 */
class VKApiDesktop : VKApi {
    val vk: VkApiClient
    val app_id: Int = 5829473
    val client_secret = "CRA8IaoOGQBBAScctuvS"
    val token_key = "token_key"
    var user: User? = null
    init {
        val transportClient = HttpTransportClient.getInstance()
        vk = VkApiClient(transportClient)
    }


    override fun authorization(code: String) {
        val authResponse = vk.oauth().serviceClientCredentialsFlow(app_id, client_secret).execute()

        val pref = Gdx.app.getPreferences("vk_pref")
        if(pref.getToken()=="") {
            val auth = vk.oauth().userAuthorizationCodeFlow(app_id,client_secret,"",code).execute()
            pref.setToken(auth.accessToken)
            pref.setUserId(auth.userId)

            println(pref.getToken())
            println(pref.getUserId())
        }
        else {
            val userActor = UserActor(pref.getUserId(), pref.getToken())
            println(vk.friends().get(userActor).execute().count)

            val serviceActor = ServiceActor(app_id, client_secret, authResponse.accessToken)
            println("friends in app:")
            vk.friends().getAppUsers(userActor).execute().forEach {
                println("${it}")
            }
            println(".\n")
            vk.friends().get(userActor).execute().items.forEach { id ->
                val levels = vk.secure().getUserLevel(serviceActor, id).execute()

                levels.forEach {
                    println("id = ${id} level = ${it.level}")
                }
            }

            //vk.secure().sendNotification(serviceActor,"Hey i'm message!")

            user = User(serviceActor, userActor, vk)
        }
    }

    override fun getUser(): VKUser? {
        return user
    }
}
class User(val actor: ServiceActor,val userActor: UserActor,vk: VkApiClient) : VKUser{
    val secure: Secure

    init {
        secure = Secure(vk)
    }
    override fun getLevel(uid: Int) : Int {
        val level = secure.getUserLevel(actor,userActor.id)

        println(level.execute()[0].toString())

        return level.execute()[0].level
    }

    override fun getMyLevel(): Int {
        return getLevel(userActor.id)
    }

    override fun setLevel(level: Int) {
        secure.setUserLevel(actor).userId(userActor.id).level(level).execute()
    }

    override fun addAppEvent(event: VKEvent) {
    }
}

fun Preferences.setToken(token: String) {
    this.putString("token",token)
    this.flush()
}
fun Preferences.getToken() : String {
    return this.getString("token")
}
fun Preferences.setUserId(id: Int) {
    this.putInteger("uid",id)
    this.flush()
}
fun Preferences.getUserId() : Int {
    return this.getInteger("uid")
}