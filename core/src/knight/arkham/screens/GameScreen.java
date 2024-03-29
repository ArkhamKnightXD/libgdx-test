package knight.arkham.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import knight.arkham.Drop;
import knight.arkham.helpers.AssetsController;
import java.util.Iterator;

//en la carpeta core iran todas las clases y methods que servirán para mi juego, ya sea para desktop o las otras
//clases que se encargara de manejar la pantalla del juego
public class GameScreen extends ScreenAdapter {

	private final Drop game = Drop.Instance;

	// las variables que almacenaran imágenes deben definirse con el tipo de dato texture
	//represents a loaded image that is stored in video ram
	private final Texture bucketImage;
	private final Texture dropImage;

	//Las variables que almacenaras sonidos fx se definen con sound y las que guardaran música se definen con music
	//La diferencia entre sound y music es que sound al ser más pequeño es guardado en memoria, en cambio,
	// music es transmitida directamente de donde este por tener mayor tamaño y no poder ser guardada en memoria
	//a rule of thumb, you should use a Sound instance if your sample is shorter than
	// 10 seconds, and a Music instance for longer audio pieces.
	private final Sound dropSound;
	private final Music rainMusic;

	//Aqui definimos la variable que sera la camara
//	private final OrthographicCamera camera;

	//para manejar la posición y tamaño del bucket necesitamos esta variable, básicamente con esto es que manejaremos
	// las imágenes en el juego
	private final Rectangle bucket;

	//preparamos un vector para guardar la posición donde toco el mouse, indicamos que es final, para solo crear
	//el vector una sola vez por proposito de performance
	private final Vector3 touchPos = new Vector3();

	//como serán varias gotas de lluvia tendremos un array de rectangle, la clase array de libgdx es
	//más eficiente en rendimiento que arraylist por eso se recomienda utilizar esto
	private final Array<Rectangle> raindrops;

//	We also need to keep track of the last time we spawned a raindrop, so we add another field:
	private long lastDropTime;

	//Almacenar cuantas gotas has conseguido
	private int gameScore;

	private int playerLives;

	//Creo asset manager para manejar los asset de esta forma, Esta es la forma ideal en la que se deben de trabajar los assets
	private final AssetManager localAssetsManager;

	//El metodo create se llama una sola vez al inicio del juego, aqui es que se deben de crear e inicializar todos
	//los elementos que tendrá esta pantalla, podemos reemplazar el metodo create por el constructor
	public GameScreen() {

		AssetsController assetsController = new AssetsController();
		localAssetsManager = assetsController.getGlobalAssetsManager();

		//la carga de los assets se debe de hacer al principio de la pantalla, ya sea en el constructor
		//o en el metodo create
		assetsController.loadAllAssetsByFolder("images");
		assetsController.loadAllAssetsByFolder("fx");
		assetsController.loadAllAssetsByFolder("music");

		//y al final indico a mi app que espere hasta que todos mis assets estén cargados
		localAssetsManager.finishLoading();

		//preparamos el arreglo y llamo a la función de raindrops
		raindrops = new Array<>();
		spawnRaindrop();

		playerLives = 3;

		//De esta forma guardo las imágenes de assets en la variables que ya definí
		bucketImage = localAssetsManager.get("images/bucket.png", Texture.class);
		dropImage = localAssetsManager.get("images/drop.png", Texture.class);

		//De esta forma guardo tanto los fx y música de los assets en las variables que ya definí
		//utilizamos el files.internal para referirnos a los elementos de nuestra carpeta assets
		dropSound = localAssetsManager.get("fx/drop.wav", Sound.class);
		rainMusic = localAssetsManager.get("music/rain.mp3", Music.class);

		//preparar que la música se ejecute desde el principio y se repita cuando acabe
		rainMusic.setVolume(0.2f);
		rainMusic.play();
		rainMusic.setLooping(true);

		//preparamos la camara
//		camera = new OrthographicCamera();

		//le indicamos las dimensiones que cubrirá la camara de nuestra pantalla
//		camera.setToOrtho(false, 800, 480);

		//definimos la posición y tamaño del bucket
		bucket = new Rectangle();

		//con estos cálculos en x centramos el bucket
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
	}


	private void spawnRaindrop() {

		//creo el rectangle que guardara la posición de mis gotas
		Rectangle raindrop = new Rectangle();

		// la posición en X la indicaré de forma aleatoria la posición en X se guardará un valor entre 0 y 800-64
		//el 64 es debido al tamaño del bucket
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;

		//agrego la gota al arreglo de gotas
		raindrops.add(raindrop);

		//guardo el momento actual en nano segundos, con esto decidiremos si lanzaremos otra gota o no
		lastDropTime = TimeUtils.nanoTime();
	}

	//Este sería el loop principal del juego el equivalente de update en unity
	@Override
	public void render (float delta) {

		//Actualizamos la camara una vez por frame
//		No tengo necesidad de utilizar camara cuando la scene solo ocupa la pantalla del juego
//		Y no hay necesidad de salir de los bordes de la pantalla
//		camera.update();

		//method that will check how much time has passed since we
		// spawned a new raindrop, and creates a new one if necessary
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		//Aqui nos encargamos de hacer descender las gotas a una velocidad constante
		makeRainFall(delta);

		//Aqui limpiaré la pantalla en cada iteración de mi gameLoop. También manejo el color de fondo que tendrá la ventana
		ScreenUtils.clear(0, 0, 0.2f, 1);

		//Aqui también preparamos la camara en el batch
//		game.batch.setProjectionMatrix(camera.combined);

		//The SpriteBatch class helps make OpenGL happy. It will record all drawing commands in between SpriteBatch.begin()
		// and SpriteBatch.end(). Once we call SpriteBatch.end() it will submit all drawing requests we made at once,
		// speeding up rendering quite a bit
		game.batch.begin();

		//dibujo las imágenes en las posiciones ya indicadas, también muestro un texto con la cantidad de gotas conseguidas
		game.font.draw(game.batch, "Drops Collected: " + gameScore, 0, 480);
		game.font.draw(game.batch, "Player Lives: " + playerLives, 336, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);

		//Aqui nos encargamos de renderizar las gotas
		for(Rectangle raindrop:  new Array.ArrayIterator<>(raindrops))
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		
		game.batch.end();

		bucketMouseMovement();
		bucketArrowKeysMovement(delta);

		//Aqui nos aseguramos que el bucket no se salga de la pantalla
		if(bucket.x < 0)
			bucket.x = 0;

		if(bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		if (playerLives == 0)
			game.setScreen(new EndGameScreen(false, localAssetsManager));

		if (gameScore == 50)
			game.setScreen(new EndGameScreen(true, localAssetsManager));

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){

			localAssetsManager.dispose();
			Gdx.app.exit();
		}
	}


	private void makeRainFall(float deltaTime) {

		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {

			Rectangle raindrop = iter.next();

//			//las gotas bajarán a esta velocidad
			if (gameScore < 15)
				raindrop.y -= 250 * deltaTime;

			if (gameScore >= 15 && gameScore < 31)
				raindrop.y -= 400 * deltaTime;

			if (gameScore >= 31)
				raindrop.y -= 550 * deltaTime;

			decrementLives(iter, raindrop);

			incrementScore(iter, raindrop);
		}
	}


	private void decrementLives(Iterator<Rectangle> iter, Rectangle raindrop) {

		//si la posición y de las gotas es menor que 0 estas serán eliminadas
		if(raindrop.y + 64 < 0){

			playerLives--;
			iter.remove();
		}
	}

	private void incrementScore(Iterator<Rectangle> iter, Rectangle raindrop) {

		//Si la gota choca con el bucket tocara el fx y eliminara la gota del arreglo
		//The Rectangle.overlaps() method checks if this rectangle overlaps with another rectangle.
		if(raindrop.overlaps(bucket)) {

			//Aumentamos la puntuación de gotas conseguidas
			gameScore++;
			dropSound.play(0.2f);
			iter.remove();
		}
	}


	private void bucketArrowKeysMovement(float deltaTime) {

		//Para mover el bucket mediante arrow keys, para manejar la velocidad de forma correcta
		// multiplicamos por la cantidad de tiempo que paso desde el último frame en este caso es el deltaTime
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			bucket.x -= 600 * deltaTime;

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			bucket.x += 600 * deltaTime;
	}


	private void bucketMouseMovement() {

		//primero preguntamos si la pantalla está siendo tocada por el mouse o un dedo
		if(Gdx.input.isTouched()) {

			//conseguimos la posición de donde el mouse toco, posición x, e Y hasta Z si fuera un game 3d
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			//To transform these coordinates to our camera’s coordinate system, we need to call the camera.unproject()
//			Esto es innecesario
//			camera.unproject(touchPos);

			//movemos el bucket a esa posición
			bucket.x = touchPos.x - 64 / 2;
		}
	}


	@Override
	public void show() {

		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
	}

//	If you don't plan to reuse the Screen instance, having screen.hide() call screen.dispose() is the perfect place to do it.
	@Override
	public void hide() {

		dispose();
	}

	//Este metodo es solo llamado cuando se cierra la aplicación
	//lo recomendable es que hagamos dispose de todos asset que creamos y también el batch
	//Asi ayudamos al sistema operativo a limpiar la memoria
	//nota el dispose cuando se implementa screen a una clase no se llama automáticamente, por lo tanto, es
	//deber de nosotros llamarlo
	@Override
	public void dispose () {

//		Disposables are usually native resources which are not handled by the Java garbage collector.
//		This is the reason why we need to manually dispose of them.
		localAssetsManager.unload("images/bucket.png");
		localAssetsManager.unload("images/drop.png");
		localAssetsManager.unload("fx/drop.wav");
		localAssetsManager.unload("music/rain.mp3");

		//y revisare metodo para cargar todos mis asset de un golpe
	}
}
