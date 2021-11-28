package knight.arkham.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class AssetsController {

    private final AssetManager globalAssetsManager = new AssetManager();


    public void loadAllAssetsByFolder(String folderName) {

        FileHandle[] files = Gdx.files.local(folderName + "/").list();

        for (FileHandle file : files) {

            switch (folderName) {

                case "images":
                    globalAssetsManager.load(file.path(), Texture.class);
                    break;

                case "fx":
                    globalAssetsManager.load(file.path(), Sound.class);
                    break;

                case "music":
                    globalAssetsManager.load(file.path(), Music.class);
                    break;
            }
        }
    }


    public AssetManager getGlobalAssetsManager() {return globalAssetsManager;}
}
