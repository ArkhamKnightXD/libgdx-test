package knight.arkham.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import knight.arkham.Drop;

public class DesktopLauncher {

	//Esta es la clase main para desktop si hubiera activado mas modulos tambien habria mas main
	public static void main (String[] arg) {

		//Con esto podemos modificar varios aspectos de la build desktop solamente
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "drop";
		config.width = 800;
		config.height = 480;

		//Aqui finalmente ejecutamos el codigo fuente de nuestro juego junto a las configuraciones que indicamos
		new LwjglApplication(new Drop(), config);
	}
}
