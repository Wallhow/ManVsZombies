package wallhow.dreamlo.sdk

import com.google.gson.*
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import wallhow.dreamlo.sdk.admin.AdminService
import wallhow.dreamlo.sdk.leaderboard.ILeaderboard
import wallhow.dreamlo.sdk.leaderboard.Leaderboard
import wallhow.dreamlo.sdk.leaderboard.LeaderboardOffline
import java.net.URI

/**
 * Created by wallhow on 23.02.17.
 */

class DreamloSDK(private val publicKey: String,private val privateKey: String) {
    val leaderboard : ILeaderboard
    val admin = AdminService()

    init {
        dreamloSDK = this

        leaderboard = if(tryConnect()) Leaderboard() else LeaderboardOffline()
    }

    private fun tryConnect() : Boolean {
        var connect = true

        try {
            publicExecute("pipe-get/2")
        }
        catch (e : Exception) {
            e.printStackTrace()
            connect = false
        }
        return connect
    }


    companion object instance {
        val gson = Gson()
        val LEADERBOARD_STORAGE_NAME = "dreamlo"
        val LEADERBOARD_RECORDS_KEY = "records"

        private lateinit var dreamloSDK: DreamloSDK
        private val httpClient = DefaultHttpClient()
        private val responseHandler = BasicResponseHandler()
        private val httpGet = HttpGet()
        private val host = "http://dreamlo.com/lb/"

        @Throws(HttpHostConnectException::class)
        private fun execute(url : String, key: String) : String {
            httpGet.uri = URI.create("$host$key/$url")
            return httpClient.execute(httpGet, responseHandler).toString()
        }

        fun privateExecute(url : String) : String {
            return execute(url, dreamloSDK.privateKey)
        }
        fun publicExecute(url : String) : String {
            return execute(url, dreamloSDK.publicKey)
        }

    }
}