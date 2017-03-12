package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 09/02/15.
 */
public class Bonus {

    private enum BonusState {
        CHANGING, NORMAL
    }

    private int size;
    private Sprite backSprite;
    private GameWorld world;
    private Vector2 position;
    private Sprite sprite;

    private TweenManager manager;

    private Circle circle;

    private Value scaleValue = new Value();
    private int angle = 0;
    private BonusState bonusState;

    private TweenCallback cbTouched;

    public Bonus(GameWorld world, Vector2 vec) {
        this.world = world;
        sprite = new Sprite(AssetLoader.bonus);
        position = new Vector2(vec);
        sprite.setPosition(vec.x, vec.y);
        this.size = 30;
        sprite.setSize(size, size);
        circle = new Circle(vec.x + (size / 2), vec.y + (size / 2), size / 2);

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        scaleValue.setValue(1);
        Tween.to(scaleValue, -1, .4f).target(.9f).repeatYoyo(100000, 0).start(manager);

        backSprite = new Sprite(AssetLoader.dot);
        backSprite.setOriginCenter();
        backSprite.setSize(size, size);
        bonusState = BonusState.NORMAL;

    }

    public void update(float delta) {
        manager.update(delta);
        angle += 3;
        sprite.setRotation(angle);
        sprite.setOriginCenter();
        sprite.setSize(scaleValue.getValue() * size, scaleValue.getValue() * size);
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getWidth() / 2);
        backSprite.setPosition(sprite.getX(), sprite.getY());
        backSprite.setSize(scaleValue.getValue() * size, scaleValue.getValue() * size);
        circle.setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        backSprite.setColor(world.colorManager.getColor());
        backSprite.draw(batch);
        sprite.setColor(world.parseColor("#27ae60", 1f));
        sprite.draw(batch);

    }

    public void isTouched(final Vector2 vec) {

        if (bonusState == BonusState.NORMAL) {
            scaleValue.setValue(1);
            cbTouched = new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    position.set(vec);
                    bonusState = BonusState.NORMAL;
                }
            };
            //Tween.to(scaleValue, -1, .3f).target(0).setCallback(cbTouched).repeatYoyo(1, 0f).setCallbackTriggers(TweenCallback.END).start(manager);
            bonusState = BonusState.CHANGING;

            Timeline.createSequence()
                    .push(Tween.to(scaleValue, -1, 0.2f).target(0).setCallback(cbTouched)
                            .setCallbackTriggers(TweenCallback.COMPLETE))
                    .push(Tween.to(scaleValue, -1, 0.5f).target(1))
                    .pushPause(1.0f)
                    .start(manager);
        }
    }

    public Circle getCircle() {
        return circle;
    }
}
