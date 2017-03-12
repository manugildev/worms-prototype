package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import gameworld.GameWorld;
import helpers.AssetLoader;

public class Orbit {

    private GameWorld world;
    private int x, y;
    private float radius;

    private Sprite sprite, backSprite;
    private Circle circle, boundingCircle;
    private Rectangle rectangle;

    public Orbit(GameWorld world, float x, float y, float radius) {
        sprite = new Sprite(AssetLoader.orbit);
        sprite.setPosition(x, y);
        sprite.setSize(radius * 2, radius * 2);
        circle = new Circle(x + radius, y + radius, radius);
        rectangle = new Rectangle(0, 0, world.worldWidth, world.worldHeight);
        boundingCircle = new Circle(circle.x, circle.y, radius - 45);
        backSprite = new Sprite(AssetLoader.dot);
        backSprite.setPosition(circle.x -radius + 10 ,circle.y - radius + 10);
        backSprite.setSize((radius-10)*2,2*(radius-10));
    }

    public void update(float delta) {
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

         backSprite.setColor(world.parseColor("#000000", 0.4f));
        backSprite.draw(batch);
        sprite.setColor(world.parseColor("#FFFFFF",1f));
        sprite.draw(batch);
    }

    public Circle getCircle() {
        return circle;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
