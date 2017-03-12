import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OrbitalsGame implements ApplicationListener {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;

    final float GAME_WORLD_WIDTH = 1280/2;
    final float GAME_WORLD_HEIGHT = 720/2;

    Viewport viewport;
    float aspectRatio;

    @Override
    public void create() {

        aspectRatio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera = new OrthographicCamera(GAME_WORLD_WIDTH,GAME_WORLD_HEIGHT);
        camera.position.set(GAME_WORLD_WIDTH/2,GAME_WORLD_HEIGHT/2,0);
        camera.update();
        //viewport = new FitViewport(GAME_WORLD_WIDTH,GAME_WORLD_HEIGHT, camera);
        //viewport.apply();

        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite = new Sprite(texture);
        sprite.setOrigin(0, 0);
        sprite.setSize(1280,720);
        sprite.setPosition(0,0);
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    camera.translate(-1f,0);
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        //viewport.update(width,height);
        //camera.position.set(0,0,0);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}
