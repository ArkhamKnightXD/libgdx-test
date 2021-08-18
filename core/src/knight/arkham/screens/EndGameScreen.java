package knight.arkham.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import knight.arkham.Drop;

public class EndGameScreen extends ScreenAdapter {

    //Inicializo mi clase game con la instancia de game que ya existe
    private final Drop game = Drop.Instance;

    private final OrthographicCamera camera;

    private final Sound playerWinSound;

    private final Sound playerLostSound;

    private final boolean playerHasWin;

    public EndGameScreen(boolean playerHasWin) {

        this.playerHasWin = playerHasWin;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        playerWinSound = Gdx.audio.newSound(Gdx.files.internal("fx/win.wav"));
        playerLostSound = Gdx.audio.newSound(Gdx.files.internal("fx/gameOver.wav"));
    }


    @Override
    public void show() {

        if (playerHasWin)
            playerWinSound.play(0.1f);

        else
            playerLostSound.play(0.1f);
    }


    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        if (playerHasWin)
            game.font.draw(game.batch, "You win the game", 100, 150);

        else
            game.font.draw(game.batch, "Game over", 100, 150);

        game.font.draw(game.batch, "Tap anywhere to restart", 100, 100);

        game.batch.end();

        //si el mouse toca la pantalla , cambiaremos la pantalla del menu hacia la del juego
        if (Gdx.input.isTouched()) {

            game.setScreen(new GameScreen());
            dispose();
        }
    }


    @Override
    public void dispose() {

        playerWinSound.dispose();
        playerLostSound.dispose();
    }
}
