package knight.arkham;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

//en la carpeta core iran todas las clases y metodos que serviran para mi juego, ya sea para desktop o las otras
public class FirstGame extends ApplicationAdapter {

	// las variables que almacenaran imagenes deben definirse con el tipo de dato texture
	//represents a loaded image that is stored in video ram
	private Texture bucketImage;
	private Texture dropImage;
	private Texture img;

	//Las variables que almacenaras sonidos fx se definen con sound y las que guardaran musica se definen con music
	//La diferencia entre sound y music es que sound al ser mas pequeño es guardado en memoria
	//en cambio music es transmitida directamente de donde este por tener mayor tamaño y no poder ser guardada en memoria
	//a rule of thumb, you should use a Sound instance if your sample is shorter than
	// 10 seconds, and a Music instance for longer audio pieces.
	private Sound dropSound;
	private Music rainMusic;

	//Aqui definimos la variable que sera la camara
	private OrthographicCamera camera;

	//The SpriteBatch is a special class that is used to draw 2D images, like the textures we loaded.
	private SpriteBatch batch;

	//para manejar la posicion y tamaño del bucket necesitamos esta variable, basicamente con esto es que manejaremos
	// las imagenes en el juego
	private Rectangle bucket;

	//seteamos un vector para guardar la posicion donde toco el mouse, indicamos que es final, para solo crear
	//el vector una sola vez por proposito de performance
	private final Vector3 touchPos = new Vector3();

	//como seran varias gotas de lluvia tendremos un array de rectangle, la clase array de libgdx es
	//mas eficiente en rendimiento que arraylist por eso se recomiendo utilizar esto
	private Array<Rectangle> raindrops;

//	We also need to keep track of the last time we spawned a raindrop, so we add another field:
	private long lastDropTime;


	private void spawnRaindrop() {

		//instancio el rectangulo que guardara la posicion de mis gotas
		Rectangle raindrop = new Rectangle();

		// la posicion en X la seteare de forma aleatoria la posicion en x se guardara un valor entre 0 y 800-64
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


	//El metodo create se llama una sola vez al inicio del juego, aqui es que se deben de crear e inicializar todos
	//los elementos que tendra nuestro juego
	@Override
	public void create () {

		//instanciamos el arreglo y llamo a la funcion de raindrops
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		//seteamos el spritebatch
		batch = new SpriteBatch();

		//De esta forma guardo las imagenes de assets en la variables que ya defini
		img = new Texture("badlogic.jpg");

		bucketImage = new Texture("bucket.png");
		dropImage = new Texture("drop.png");

		//De esta forma guardo tanto los fx y musica de los assets en las variables que ya defini
		//utilizamos el files.internal para referirnos a los elementos de nuestra carpeta assets
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		//setear que la musica se ejecute desde el principio y se repita cuando acabe
		rainMusic.play();
		rainMusic.setLooping(true);

		//seteamos la camara
		camera = new OrthographicCamera();

		//le indicamos las dimensiones de nuestra pantalla
		camera.setToOrtho(false, 800, 400);

		//definimos la posicion y tamaño del bucket
		bucket = new Rectangle();

		//con esto calculos en x centramos el bucket
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
	}

	//Este seria el loop principal del juego el equivalente de update en unity
	@Override
	public void render () {

		//Actualizamos la camara una vez por frame
		camera.update();

		//method that will check how much time has passed since we
		// spawned a new raindrop, and creates a new one if necessary
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		//Aqui nos encargamos de hacer descender las gotas a una velocidad constante
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {

			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

			if(raindrop.y + 64 < 0) iter.remove();

			//Si la gota choca con el bucket tocara el fx y eliminara la gota del arreglo
			//The Rectangle.overlaps() method checks if this rectangle overlaps with another rectangle.
			if(raindrop.overlaps(bucket)) {

				dropSound.play();
				iter.remove();
			}
		}


		//Aqui manejo el color de fondo que tendra la ventana
		ScreenUtils.clear(100, 100, 100, 1);

		//Aqui tambien seteamos la camara en el batch
		batch.setProjectionMatrix(camera.combined);

		//The SpriteBatch class helps make OpenGL happy. It will record all drawing commands in between SpriteBatch.begin()
		// and SpriteBatch.end(). Once we call SpriteBatch.end() it will submit all drawing requests we made at once,
		// speeding up rendering quite a bit
		batch.begin();

		//dibujo las imagenes en las posiciones ya indicadas
		batch.draw(bucketImage, bucket.x, bucket.y);

		//Aqui nos encargamos de renderizar las gotas
		for(Rectangle raindrop: raindrops) {

			batch.draw(dropImage, raindrop.x, raindrop.y);
		}

		batch.end();

//		If the user touches the screen (or presses a mouse button), we want the bucket
//		to center around that position horizontally

		//primero preguntamos si la pantalla esta siendo tocada por el mouse o un dedo
		if(Gdx.input.isTouched()) {

			//conseguimos la posicion de donde el mouse toco, posicion x, y y hasta z si fuera un game 3d
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			//To transform these coordinates to our camera’s coordinate system, we need to call the camera.unproject()
			camera.unproject(touchPos);

			//movemos el bucket a esa posicion
			bucket.x = touchPos.x - 64 / 2;
		}

		//Para mover el bucket mediante arrow keys, para manejar la velocidad de forma correcta
		// multiplicamos por la cantidad de tiempo que paso desde el ultimo frame en este caso es el deltaTime
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 400 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 400 * Gdx.graphics.getDeltaTime();

		//Aqui nos aseguramos que el bucket no se salga de la pantalla
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;
	}

	//Este metodo es solo llamado cuando se cierra la aplicacion
	//lo recomendable es que hagamos dispose de todos asset que instanciamos y tambien el batch
	//Asi ayudamos al sistema operativo a limpiar la memoria
	@Override
	public void dispose () {

//		Disposables are usually native resources which are not handled by the Java garbage collector.
//		This is the reason why we need to manually dispose of them.
		batch.dispose();
		img.dispose();
		rainMusic.dispose();
		dropSound.dispose();
		dropImage.dispose();
		bucketImage.dispose();
	}
}
