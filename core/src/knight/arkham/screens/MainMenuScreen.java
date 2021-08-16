package knight.arkham.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import knight.arkham.Drop;

//clase encarga de manejar la pantalla de el menu principal del juego debe de implementar Screen
public class MainMenuScreen implements Screen {

    //Realizo inyeccion de dependencia de mi clase juego
    private final Drop game;

    private final OrthographicCamera camera;

    public MainMenuScreen(final Drop game) {

        this.game = game;

        //En el menu principal tambien debemos setear otra camara, en cada pantalla esto debe de ser asi
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }


    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

//        Aqui utilizaremos el batch y el font de la clase game
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        //dibujamos el texto en la pantalla
        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);

        game.batch.end();

        //si el mouse toca la pantalla , cambiaremos la pantalla del menu hacia la del juego
        if (Gdx.input.isTouched()) {

            game.setScreen(new GameScreen(game));
            dispose();
        }

    }

    //todos estos metodos deben de ser implementados por utilizar screen
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
