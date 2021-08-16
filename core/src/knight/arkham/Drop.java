package knight.arkham;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import knight.arkham.screens.MainMenuScreen;

//Esta sera mi game class
//The Game class is responsible for handling multiple screens and
// provides some helper methods for this purpose,
public class Drop extends Game {

    //Estos elementos seran publicos para llamarse en el mainmenuscreen
    //En resumen para todas las pantallas debemos utilizar estos font y batch
    public SpriteBatch batch;

//    BitmapFont object is used, along with a SpriteBatch, to render text onto the screen.
    public BitmapFont font;

    @Override
    public void create() {

        batch = new SpriteBatch();
        font = new BitmapFont();

//        Next, we set the Screen of the Game to a MainMenuScreen object,
//        with a Drop instance as its first and only parameter.
        this.setScreen(new MainMenuScreen(this));
    }


    //Why?
    public void render() {
        super.render(); // important!
    }

    public void dispose() {

        batch.dispose();
        font.dispose();
    }

}
