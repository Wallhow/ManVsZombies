package wallhow.manvszombies.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.google.inject.Inject
import wallhow.acentauri.ashley.components.CImage
import wallhow.acentauri.ashley.components.CPosition
import wallhow.manvszombies.game.Game
import wallhow.manvszombies.game.components.CHealth

/**
 * Created by wallhow on 22.01.17.
 */
class DrawHealthSystem @Inject constructor(val batch: SpriteBatch) : IteratingSystem(Family.all(CHealth::class.java).get()) {
    private val p : Texture
    private val border = 4
    private val halfBorder = border /2
    init {
        p = Texture(Pixmap(1,1,Pixmap.Format.RGBA8888).apply {
            val c =Color.WHITE.cpy()
            c.a = 0.5f
            setColor(c)
            fill()
        })
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (CHealth[entity].draw) {
            val heightLine = CImage[entity].height
            val widthLine = 6f
            val step = (heightLine - halfBorder) / CHealth[entity].maxHealth
            val pos = CPosition[entity].position

            batch.color = Color.BLUE
            batch.draw(p, pos.x, pos.y, widthLine + border, heightLine)
            batch.color = Color.GOLD
            batch.draw(p, pos.x + halfBorder, pos.y + halfBorder, widthLine - halfBorder, step * CHealth[entity].currentHealth)
            batch.color = Color.WHITE
            Game.ttfFont.get(10).draw(batch,"${CHealth[entity].currentHealth.toInt()}/${CHealth[entity].maxHealth.toInt()}",pos.x,pos.y)
        }
    }

    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }
}