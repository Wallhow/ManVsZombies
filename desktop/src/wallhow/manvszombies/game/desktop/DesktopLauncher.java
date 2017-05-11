package wallhow.manvszombies.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import wallhow.manvszombies.game.Game;
import wallhow.manvszombies.game.desktop.service.VKGameService;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		config.title = "ManVsZombies";
		//https://oauth.vk.com/authorize?client_id=5829473&display=mobile&scope=friends,photos,wall,notify,offline&response_type=code&v=5.62

		new LwjglApplication(new Game(new VKGameService()), config);
	}
}
