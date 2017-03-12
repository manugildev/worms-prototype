package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 05/02/15.
 */
public class Enemy {


    private GameWorld world;

    private Sprite sprite, backSprite;
    private Vector2 position, velocity, acceleration;
    private int height;

    //VARIABLES
    private Circle circle;

    //TWEEN
    private TweenManager manager;
    private Value distValue = new Value();

    private HeroState heroState;
    private float angle, angleVel;

    private float radius;

    private int orbitNumber;
    private Vector2 heroCamera = new Vector2();

    private ParticleEffect particleEffect, effect;

    private Vector2 point = new Vector2();
    private Value angleVelValue = new Value();
    private Value angleValue = new Value();

    private Circle circle1;
    private float randomVel = (float) (Math.random() * 0.4 + 0.6);

    public Enemy(GameWorld world, float x, float y, float radius) {

        //GENERAL VARIABLES
        this.world = world;

        //Position
        this.position = new Vector2(x, y);

        //Sprite
        sprite = new Sprite(AssetLoader.hero);
        backSprite = new Sprite(AssetLoader.dot);
        backSprite.setOriginCenter();
        backSprite.setSize(radius * 2, radius * 2);

        sprite.setOriginCenter();
        sprite.setPosition(position.x, position.y);
        sprite.setSize(radius * 2, radius * 2);
        this.radius = radius;

        //TWEENS STUFF
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        //STARTING TWEENS
        heroState = HeroState.IDLE;

        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);

        orbitNumber = 0;
        heroCamera = new Vector2();

        distValue.setValue(world.getOrbits().get(orbitNumber).getSprite().getWidth() / 2);

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("emitterEnemy.p"), Gdx.files.internal(""));

        angleVelValue.setValue(-120);
        Tween.to(angleVelValue, -1, 0.3f).target(120).ease(TweenEquations.easeInOutSine)
                .repeatYoyo(10000, 0f).start(manager);

        circle = new Circle();
        circle1 = world.getOrbits().get(0).getBoundingCircle();
        heroState = HeroState.IDLE;

        angle = getAngle2Vecs(new Vector2(position.x, position.y),
                new Vector2(world.getOrbits().get(0).getCircle().x,
                        world.getOrbits().get(0).getCircle().y));
        angle = (float) (Math.random() * 360);
        //Gdx.app.log("Angle", angle + "");
        //angleValue .setValue(-180);

        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("emitter.p"), Gdx.files.internal(""));
        particleEffect.setPosition(-500, -500);
    }


    public void update(float delta) {
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
        manager.update(delta);

        particleEffect.update(delta);
        effect.setPosition(sprite.getX() + radius, sprite.getY() + radius);
        effect.update(delta);
        point.set(calculatePosition(radius + 250, angle));

        velocity.set((point.x - (sprite.getX() + (sprite.getWidth() / 2))),
                (point.y - (sprite.getY() + (sprite.getWidth() / 2))));

        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));
        sprite.setPosition(position.x, position.y);
        circle.set(position.x + radius, position.y + radius, radius);
        backSprite.setPosition(sprite.getX(), sprite.getY());

        if (heroState != HeroState.OUT_OF_CIRCLE) {
            angle += (angleVel + angleVelValue.getValue()) * delta;
        } else {
            angle = angleValue.getValue();
        }
        //Bounce out of the circle
        outOfCircle();
        //}
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {
            particleEffect.draw(batch);
            effect.draw(batch);
            backSprite.setColor(world.colorManager.getColor());
            backSprite.draw(batch);
            sprite.setColor(world.parseColor("#e74c3c", 1f));
            sprite.draw(batch);
            /*batch.end();shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(circle.x,circle.y,circle.radius);
            shapeRenderer.end();
            batch.begin();*/
        }
    }

    private void outOfCircle() {
        if (heroState != HeroState.OUT_OF_CIRCLE) {
            if (!Intersector.overlaps(circle, circle1)) {
                heroState = HeroState.OUT_OF_CIRCLE;
                returnToCircle();
                if (particleEffect.isComplete()) {
                    particleEffect.setPosition(circle.x, circle.y);
                    particleEffect.start();
                }
            }
        } else {
            if (Intersector.overlaps(circle, circle1)) {
                heroState = HeroState.IDLE;
            }
        }
    }

    private void returnToCircle() {
        angleValue.setValue(angle);
        angleVel = 0;
        int target = (int) (160 + Math.random() * 40);
        target = (int) getAngle2Vecs(new Vector2(position.x, position.y),
                new Vector2(world.getOrbits().get(0).getCircle().x,
                        world.getOrbits().get(0).getCircle().y));
        if (Math.random() < 0.5f) {
            target -= 20;
        } else {
            target += 20;
        }
        Tween.to(angleValue, -1, 0.1f).target(target).ease(TweenEquations.easeInOutSine)
                .repeatYoyo(0, 0f).start(manager);
    }


    private Vector2 calculatePosition(float distanceToCenter, float angle) {
        float cx = sprite.getX() + sprite.getWidth() / 2;
        float cy = sprite.getY() + sprite.getWidth() / 2;
        return new Vector2((float) (cx + distanceToCenter
                * Math.sin(Math.toRadians(angle))),
                (float) (cy + distanceToCenter * Math.cos(Math.toRadians(angle))));
    }

    public void clickRight() {
        angleVel = -300;
    }

    public void clickLeft() {
        angleVel = 300;
    }

    public void clickUp() {
    }

    public void clickDown() {
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void noClick() {
        angleVel = 0;
    }

    public Vector2 getPoint() {
        return heroCamera;
    }

    public float getAngle2Vecs(Vector2 vec1, Vector2 vec) {

        float angle1 = (float) Math.toDegrees(Math.atan2(vec1.y - vec.y, vec.x - vec1.x));

        return angle1 + 90;

    }

    public Circle getCircle() {
        return circle;
    }
}
