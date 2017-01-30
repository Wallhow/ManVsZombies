package wallhow.acentauri.utils

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.ObjectMap
import wallhow.acentauri.utils.ozmod.ChipPlayer
import wallhow.acentauri.utils.ozmod.OZMod

/**
 * Created by zhokin on 16.05.2016.
 */
class APlayer {
    val ozMod = OZMod()
    private var frequency = intArrayOf(44100,48000,96000)
    private var listMusic = ObjectMap<String,FileHandle>()
    var chipPlayer: ChipPlayer? = null

    init {
        ozMod.initOutput()
    }


    fun addMusic(file: String,descriptor: String = file){
        val module = FileHandle(file)
        listMusic.put(descriptor,module)
    }
    fun play(descriptor: String) {
        try {
            chipPlayer?.done()
            chipPlayer = ozMod.getPlayer(listMusic.get(descriptor))
            chipPlayer?.setDaemon(true)
            chipPlayer?.isLoopable = false
            chipPlayer?.setFrequency(frequency[0])
        }
        catch (e: Exception) {
            System.out.println(" не удалось загрузить $descriptor")
        }

        chipPlayer?.play()
    }


}