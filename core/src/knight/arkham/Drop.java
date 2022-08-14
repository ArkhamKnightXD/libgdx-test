package knight.arkham;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import knight.arkham.screens.MainMenuScreen;

//Esta sera mi game class
//The Game class is responsible for handling multiple screens and
// provides some helper methods for this purpose,
public class Drop extends Game {

    //lo recomendable es implementar el metodo singleton en la clase game, pues esta se utilizara en todas las pantallas
    //Es un poco diferente a como normalmente se utiliza en otros proyectos
    public static Drop Instance;

    //The SpriteBatch is a special class that is used to draw 2D images, like the textures we loaded.
    //Estos elementos seran publicos para llamarse en el mainmenuscreen
    //En resumen para todas las pantallas debemos utilizar estos font y batch
    public SpriteBatch batch;

//    BitmapFont object is used, along with a SpriteBatch, to render text onto the screen.
    public BitmapFont font;

    //constructor necesario para poder utilizar singleton
    public Drop() {

        Instance = this;
    }

    @Override
    public void create() {

        batch = new SpriteBatch();
        font = new BitmapFont();

//        Next, we set the Screen of the Game to a MainMenuScreen object,
        setScreen(new MainMenuScreen());
    }

    //A common mistake is to forget to call super.render() with a Game implementation. Without this call, the Screen that
    // you set in the create() method will not be rendered if you override the render method in your Game class!
    public void render() {
        super.render(); // important!
    }

}
