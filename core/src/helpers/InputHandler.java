package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

import gameworld.GameState;
import gameworld.GameWorld;
import ui.SimpleButton;

public class InputHandler implements InputProcessor {

    private GameWorld world;
    private ArrayList<SimpleButton> menuButtons;
    private float scaleFactorX;
    private float scaleFactorY;
    private int activeTouch = 0;


    public InputHandler(GameWorld world, float scaleFactorX,
                        float scaleFactorY) {
        this.world = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Keys.LEFT) {
            world.getHero().clickRight();
        } else if (keycode == Keys.RIGHT) {
            world.getHero().clickLeft();
        } else if (keycode == Keys.UP) {
            world.getHero().clickUp();
        } else if (keycode == Keys.DOWN) {
            world.getHero().clickDown();
        } else if (keycode == Keys.R) {
            world.renewMap();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.LEFT) {
            world.getHero().noClick();
        } else if (keycode == Keys.RIGHT) {
            world.getHero().noClick();
        } else if (keycode == Keys.UP) {
            world.getHero().clickUp();
        } else if (keycode == Keys.DOWN) {
            world.getHero().clickDown();
        } else if (keycode == Keys.R) {
            world.renewMap();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        activeTouch++;
        if (world.isRunning()) {
            if (screenX >= world.gameWidth / 2) {
                world.getHero().noClick();
                world.getHero().clickLeft();

            } else {
                world.getHero().noClick();
                world.getHero().clickRight();

            }
        }else if(world.isPaused()){
            world.gameState = GameState.RUNNING;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        activeTouch--;
        if (activeTouch == 0) {
            world.getHero().noClick();
        } else {
            if (screenX >= world.gameWidth / 2) {
                world.getHero().noClick();
                world.getHero().clickRight();
            } else {
                world.getHero().noClick();
                world.getHero().clickLeft();
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

}
