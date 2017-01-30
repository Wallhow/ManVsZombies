package wallhow.acentauri.ashley.components

import com.badlogic.ashley.core.Component

/**
 * Created by wallhow on 20.12.16.
 */
class CameraLookComponent : Component {
    companion object : ComponentResolver<CameraLookComponent>(CameraLookComponent::class.java)
}