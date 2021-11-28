package knight.arkham.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import knight.arkham.Drop;

public class DesktopLauncher {

	//Esta es la clase main para desktop si hubiera activado mas modulos tambien habria mas main
	public static void main (String[] arg) {

		//Con esto podemos modificar varios aspectos de la build desktop solamente
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//Aqui seteamos el titulo de la pantalla junto al tamaño que tendra
		config.title = "drop";
		config.width = 800;
		config.height = 480;

		//Con esto evitamos el molesto error que sale cuando cerramos la aplicacion de forma forzada
		//ya sea cerrando la ventana o presionando escape si esta configurado
		config.forceExit = false;

		//Aqui finalmente ejecutamos el codigo fuente de nuestro juego junto a las configuraciones que indicamos
		new LwjglApplication(new Drop(), config);
	}
}
