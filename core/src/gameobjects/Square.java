package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 05/02/15.
 */
public class Square implements Pool.Poolable {

    private GameWorld world;
    private Sprite sprite;
    private float width, height;

    private Vector2 position, velocity;
    private int type;

    private Rectangle rectCol;
    public Rectangle leftRect, rightRect, topRect;

    private int maxVelocity = 800;

    public boolean alive;

    public Square(GameWorld world, int i, float x) {
        this.world = world;
        this.sprite = new Sprite(AssetLoader.square);
        width = (float) ((Math.random() * (world.gameWidth / 2)) + 200);
        this.type = type;
        this.position = new Vector2(x,0);
        height = -position.y + world.gameHeight;
        this.sprite.setSize(width, height);
        this.velocity = new Vector2(-maxVelocity, 0);
        this.sprite.setPosition(position.x, position.y);
        rectCol = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(),
                sprite.getHeight());

        this.alive = true;

        rightRect = new Rectangle();
        leftRect = new Rectangle();
        topRect = new Rectangle();
    }

    public void init(int i, Array<Square> activeSquares) {
        //this.position.x = (float) ((Math.random() * 10000) + world.gameWidth);
        this.position.y = (float) ((Math.random() * (world.gameHeight - 400)) + 250);
        height = -position.y + world.gameHeight;
        width = (float) ((Math.random() * (world.gameWidth / 1.5)) + 200);
        alive = true;

        if (i == 0) {
            Sprite previous = activeSquares.get(activeSquares.size - 1).getSprite();
            this.position.x = (float) (previous.getX() + previous.getWidth() + (Math
                    .random() * 300 + 200));
        } else {
            Sprite previous = activeSquares.get(i - 1).getSprite();
            this.position.x = (float) (previous.getX() + previous.getWidth() + (Math
                    .random() * 300 + 200));
        }

        sprite.setPosition(this.position.x, this.position.y);
        sprite.setSize(width, height);
    }


    public void update(float delta) {
        if (alive) {
            position.add(velocity.cpy().scl(delta));
            sprite.setPosition(position.x, position.y);
            rectCol.setPosition(position);
            rightRect.set(position.x, position.y + 20, 20, height);
            leftRect.set(position.x + width - 20, position.y + 20, 20, height);
            topRect.set(position.x + 10, position.y, width - 20, 20);
        }


    }

    public boolean isOutOfScreen() {
        if (this.sprite.getX() < 0 - (width)) {
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        sprite.setColor(Color.GREEN);
        sprite.draw(batch);

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(topRect.x, topRect.y, topRect.width, topRect.height);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(leftRect.x, leftRect.y, leftRect.width, leftRect.height);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(rightRect.x, rightRect.y, rightRect.width, rightRect.height);
        shapeRenderer.end();
        batch.begin();
    }

    public Rectangle getRectCol() {
        return rectCol;
    }

    @Override
    public void reset() {
        position.set(0, 0);
        alive = false;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getLeftRect() {
        return leftRect;
    }

    public Rectangle getRightRect() {
        return rightRect;
    }

    public Rectangle getTopRect() {
        return topRect;
    }

}
