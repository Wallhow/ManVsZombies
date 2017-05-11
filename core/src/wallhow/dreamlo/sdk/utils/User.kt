package wallhow.dreamlo.sdk.utils

class User : Comparable<User> {
    var name : String = ""
    var score : Int = 0
    var seconds : Int = 0
    var text : String = ""
    var date : String = ""

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[ $name ]")
        sb.append("\n[ score = $score, seconds = $seconds, text = $text, date = $date ]")
        return sb.toString()
    }

    companion object instance {
        fun create(pipeString: String, charSplit : String) : User {
            val strs = pipeString.split(charSplit)
            val user = User()
            user.name = strs[0]
            user.score = strs[1].toInt()
            user.seconds = strs[2].toInt()
            user.text = strs[3]
            user.date = strs[4]
            return user
        }
        val NOT_FOUND = "UserNotFound"
    }
    override fun compareTo(other: User): Int {
        if(other.score < score) return -1
        else if(other.score > score) return 1
        else return 0
    }
}