package wallhow.acentauri.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.*
import com.badlogic.gdx.utils.Array
import java.io.IOException

/**
 * Created by wallhow on 18.11.16.
 */
class TTFFont(path : String) {
    private val fonts = ArrayMap<Int,BitmapFont>()
    private val gen = FreeTypeFontGenerator(Gdx.files.internal(path))
    private val generatorConf = FreeTypeFontGenerator.FreeTypeFontParameter()
    private val FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>"
    init {
        generatorConf.color = Color.WHITE
        //loadFromFile("")
    }

    fun createFromParameterArray(parameterArray: FontParameterArray) {
        parameterArray.fontParameters.forEach {
            if(fonts.get(it.size) == null) {
                generatorConf.size = it.size
                generatorConf.color = it.color
                generatorConf.characters = FONT_CHARS
                val font = gen.generateFont(generatorConf)
                fonts.put(it.size,font)
            }
        }
    }

    fun createFont(size: Int, color: Color) : TTFFont {
        if(fonts.get(size) == null) {
            generatorConf.size = size
            generatorConf.color = color
            generatorConf.characters = FONT_CHARS
            val font = gen.generateFont(generatorConf)
            fonts.put(size,font)
        }
        return this
    }

    fun get(size : Int): BitmapFont {
        if(fonts.get(size) == null) {
            generatorConf.size = size
            generatorConf.characters = FONT_CHARS
            val font = gen.generateFont(generatorConf)
            fonts.put(size,font)
            return font
        }
        else {
            return fonts.get(size)
        }
    }

    private fun loadFromFile(path: String) {
        try {

        }
        //Если твой там кто-то там что-то, то это просто невозможно!
        catch (e: IOException) {

        }
    }
    data class FontParameter(val size: Int, val color: Color)
    data class FontParameterArray(val fontParameters: Array<FontParameter>)
    fun fontParameterArray(vararg fontParameter: FontParameter) : FontParameterArray {
        return FontParameterArray(Array<FontParameter>().apply {
            fontParameter.forEach {
                fontParameter -> this.add(fontParameter)
            }
        })
    }

    data class Font(val name: String, val path: String, val fontParameters: FontParameterArray)

}