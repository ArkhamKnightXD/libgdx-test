package knight.arkham.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import knight.arkham.Drop;

//clase encarga de manejar la pantalla de el menu principal del juego debe de implementar Screen
public class MainMenuScreen extends ScreenAdapter {

    //Realizo inyeccion de dependencia de mi clase juego
    private final Drop game = Drop.Instance;

    public MainMenuScreen() {

        //Para pantallas estaticas no es necesario utilizar camaras
    }


    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.batch.begin();

        //dibujamos el texto en la pantalla
        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);

        game.batch.end();

        //si el mouse toca la pantalla , cambiaremos la pantalla del menu hacia la del juego
        if (Gdx.input.isTouched()) {

            game.setScreen(new GameScreen());
            dispose();
        }
    }

}
