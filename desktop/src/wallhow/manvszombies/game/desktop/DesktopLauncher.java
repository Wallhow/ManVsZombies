package wallhow.manvszombies.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import wallhow.manvszombies.game.Game;
import wallhow.manvszombies.game.desktop.service.VKGameService;

import java.util.Timer;
import java.util.TimerTask;

public class DesktopLauncher extends Application {
	static Timer timer;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		config.title = "ManVsZombies";

		timer = new Timer();
		//launch(arg);
		new LwjglApplication(new Game(new VKGameService()), config);
	}

	@Override
	public void start(Stage primaryStage) {
		System.out.println("launch FX");
		primaryStage.setTitle("Login Browser");
		primaryStage.setMinWidth(400);
		primaryStage.setMinHeight(400);


		WebView webView = new WebView();
		webView.getEngine().load(
				"https://oauth.vk.com/authorize?client_id=5829473&display=mobile&scope=friends,photos,wall,notify,offline&response_type=code&v=5.62");
		webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				primaryStage.setTitle(webView.getEngine().getLocation());
				String url = webView.getEngine().getLocation();
				if(url.contains("code=")) {
					String code = url.substring(url.indexOf("=")+1);
					System.out.println(code);
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							Gdx.app.getPreferences("service").putString("code",code).flush();
						}
					},4000);

					primaryStage.close();
				}
			}
		});
		Scene scene = new Scene(webView);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
