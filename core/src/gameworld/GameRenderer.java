package gameworld;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import gameobjects.Bonus;
import gameobjects.Enemy;
import gameobjects.Hero;
import gameobjects.Orbit;
import gameobjects.Square;
import gameobjects.Star;

public class GameRenderer {

    private GameWorld world;
    private final ShapeRenderer shapeRenderer;
    private ShaderProgram fontShader;

    //GAME OBJECTS
    private Hero hero;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    private Array<Star> stars = new Array<Star>();

    private Array<Enemy> enemies = new Array<Enemy>();

    private Array<Orbit> orbits = new Array<Orbit>();
    private Bonus bonus;

    BitmapFont font = new BitmapFont();

    public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {
        this.world = world;

        camera = new OrthographicCamera(world.gameWidth, world.gameHeight);
        camera.position.set(world.worldWidth / 2, world.worldHeight / 2, 0);
        camera.update();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        initObjects();
        initFont();
    }

    private void initObjects() {
        hero = world.getHero();
        stars = world.getStars();
        enemies = world.getEnemies();
        orbits = world.getOrbits();
        bonus = world.getBonus();
        //squares = world.getSquareArray();
    }

    private void initFont() {
        fontShader = new ShaderProgram(Gdx.files.internal("font.vert"),
                Gdx.files.internal("font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader",
                    "compilation failed:\n" + fontShader.getLog());
        }
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(world.colorManager.getColor().r, world.colorManager.getColor().g,
                world.colorManager.getColor().b, world.colorManager.getColor().a);
        //Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);


        //camera.position.set(new Vector3(hero.getSprite().getX(), hero.getSprite().getY(), 0));

        camera.position.lerp(new Vector3(hero.getPoint().x, hero.getPoint().y, 0), 0.1f);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();

        for (int i = 0; i < stars.size; i++) {
            stars.get(i).render(batch, shapeRenderer);
        }
        //sprite.draw(batch);
        //world.getOrbit().render(batch, shapeRenderer);
        orbits.get(0).render(batch, shapeRenderer);


        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(batch, shapeRenderer);
        }

        font.setScale(4);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(),
                camera.position.x - world.gameWidth / 2 + 100,
                camera.position.y - world.gameHeight / 2 + 100);
        bonus.render(batch, shapeRenderer);
        hero.render(batch, shapeRenderer);
        //world.getEnemy().render(batch, shapeRenderer);
        batch.end();
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        for (int i = 0; i < world.getPoints().size; i++) {
            //shapeRenderer.circle(world.getPoints().get(i).x, world.getPoints().get(i).y, 10);
        }
        shapeRenderer.end();
*/
    }

    private boolean cameraInsideWorld() {
        Gdx.app.log("CameraPos", camera.position.toString());
        return false;
    }

}
