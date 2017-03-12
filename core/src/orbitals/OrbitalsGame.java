package orbitals;


import com.badlogic.gdx.Game;

import helpers.AssetLoader;
import orbitals.ActionResolver;
import screens.SplashScreen;

public class OrbitalsGame extends Game {

    private ActionResolver actionresolver;


    public OrbitalsGame(ActionResolver actionresolver) {
        this.actionresolver = actionresolver;
    }

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new SplashScreen(this,actionresolver));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
