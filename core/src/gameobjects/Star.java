package gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 09/02/15.
 */
public class Star {
    private Vector2 position, velocity;
    private Sprite sprite;

    private GameWorld world;

    public Star(GameWorld world) {
        this.world = world;
        sprite = new Sprite(AssetLoader.dot);
        float size = (float) Math.random() * 5 + 5;
        sprite.setSize(size, size);
        sprite.setPosition((float) Math.random() * world.worldWidth,
                (float) Math.random() * world.worldHeight);
        position = new Vector2(sprite.getX(), sprite.getY());
        velocity = new Vector2((float) Math.random() * 40, (float) Math.random() * 40);
        if (Math.random() < 0.5f) {
            velocity.x = velocity.x * -1;
        }
        if (Math.random() < 0.5f) {
            velocity.y = velocity.y * -1;
        }

    }

    public void update(float delta) {
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
            position.add(velocity.cpy().scl(delta));
            sprite.setPosition(position.x, position.y);
            if (sprite.getX() < 0 || sprite.getX() > world.worldWidth) {
                velocity.x = velocity.x * -1;
            }
            if (sprite.getY() < 0 || sprite.getY() > world.worldHeight) {
                velocity.y = velocity.y * -1;
            }
       // }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {
            sprite.setColor(world.parseColor("#FFFFFF", 0.1f));
            sprite.draw(batch);
        }
    }
}
