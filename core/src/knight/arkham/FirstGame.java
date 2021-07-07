package knight.arkham;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FirstGame extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {

		//Aqui manejo el color de fondo que tendra la ventana
		ScreenUtils.clear(100, 100, 100, 1);

		//Aqui dibujo la imagen y indico en que position estara
		batch.begin();
		batch.draw(img, 180, 150);
		batch.end();
	}
	
	@Override
	public void dispose () {

		batch.dispose();
		img.dispose();
	}
}
