package gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import gameobjects.Bonus;
import gameobjects.Enemy;
import gameobjects.Hero;
import gameobjects.Orbit;
import gameobjects.Square;
import gameobjects.Star;
import helpers.ColorManager;
import orbitals.ActionResolver;
import orbitals.OrbitalsGame;

/**
 * Created by ManuGil on 15/01/15.
 */
public class GameWorld {

    //GENERAL VARIABLES
    public float gameWidth;
    public float gameHeight;
    public float worldWidth;
    public float worldHeight;

    public ActionResolver actionResolver;
    public OrbitalsGame game;
    public GameWorld world = this;

    //GAME OBJECTS
    private Hero hero;
    private Orbit orbit;
    private Enemy enemy;
    public GameState gameState;
    private Bonus bonus;

    public ColorManager colorManager;

    //ORBITS
    private int distanceBetweenOrbits;
    private final int numberOfOrbits;
    private final int numberOfStars;
    private int numOfEnemies;

    private Array<Orbit> orbits = new Array<Orbit>();
    private Array<Enemy> enemies = new Array<Enemy>();


    private Array<Star> stars = new Array<Star>();

    float orbitR;
    private Array<Vector2> points;

    private int score;

    public GameWorld(OrbitalsGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, float worldWidth, float worldHeight) {

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.game = game;
        this.actionResolver = actionResolver;
        this.distanceBetweenOrbits = 0;
        this.numOfEnemies = 20;
        this.numberOfOrbits = 1;
        this.numberOfStars = 500;
        orbitR = worldHeight / 2 - 500;
        colorManager = new ColorManager();


        //orbit = new Orbit(this, worldWidth / 2 - orbitR, 0, orbitR);


        for (int i = 0; i < numberOfOrbits; i++) {
            orbits.add(
                    new Orbit(this, worldWidth / 2 - orbitR,
                            world.worldHeight / 2 - orbitR,
                            orbitR + ((i) * distanceBetweenOrbits)));
        }
        hero = new Hero(this, worldWidth / 2, worldHeight / 2, 10);
        createPoints(100);

        enemies.clear();
        for (int i = 0; i < numOfEnemies; i++) {
            int random = (int) Math.floor(Math.random() * points.size);
            enemy = new Enemy(this, points.get(random).x, points.get(random).y, 10);
            enemies.add(enemy);
        }

        gameState = GameState.RUNNING;

        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star(world));
        }

        bonus = new Bonus(world, points.get((int) Math.floor(Math.random() * points.size)));
    }

    /*private boolean overlapping() {
        for (int i = 0; i < numOfEnemies; i++) {
            for (int j = 0; j < numOfEnemies; j++) {
                Vector2 vec1 = new Vector2(enemies.get(i).getCircle().x,
                        enemies.get(i).getCircle().y);
                Vector2 vec2 = new Vector2(enemies.get(j).getCircle().x,
                        enemies.get(j).getCircle().y);
                double distance = Math
                        .sqrt(Math.pow((vec2.x - vec1.x), 2) + Math.pow((vec2.y - vec1.y), 2));

                if (distance < 0 && distance != 0) {
                    Gdx.app.log("Distance", distance + " ");
                    return true;
                }

            }
        }
        return false;
    }
*/
    public void update(float delta) {
        colorManager.update(delta);


        bonus.update(delta);
        //enemy.update(delta);
        //orbit.update(delta);
        for (int i = 0; i < orbits.size; i++) {
            orbits.get(i).update(delta);
        }

        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(delta);
        }

        for (int i = 0; i < numberOfStars; i++) {
            stars.get(i).update(delta);
        }
        if (isRunning()) {
            collisions();
            hero.update(delta);
        }
    }

    private void collisions() {
        if (Intersector.overlaps(hero.getCircle(), bonus.getCircle())) {
            bonus.isTouched(points.get((int) Math.floor(Math.random() * points.size)));
        }
        for (int i = 0; i < enemies.size; i++) {
            if (Intersector.overlaps(hero.getCircle(), enemies.get(i).getCircle())) {
                gameState = GameState.PAUSE;
                renewMap();
                finish();
            }
        }

    }

    private void finish() {
    }


    public Hero getHero() {
        return hero;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Array<Orbit> getOrbits() {
        return orbits;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public void renewMap() {
        enemies.clear();
        for (int i = 0; i < numOfEnemies; i++) {
            int random = (int) Math.floor(Math.random() * points.size);
            enemy = new Enemy(this, points.get(random).x, points.get(random).y, 10);
            enemies.add(enemy);
        }
    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
            // Gdx.app.log("Hex", hex1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public void createPoints(int numOfS) {
        points = new Array<Vector2>();
        points.clear();

        for (int i = 0; i < numOfS; i++) {
            int distanceToCenter = (int) ((int) (orbitR - 50) * Math.random());

            float cx = orbits.get(0).getCircle().x;
            float cy = orbits.get(0).getCircle().y;
            double angle = (i * (360 / numOfS));

            //Gdx.app.log("Point " + i, angle + " ");
            Vector2 point = new Vector2((int) (distanceToCenter * Math.cos(Math
                    .toRadians(angle - 90))) + cx, (int) (distanceToCenter
                    * Math.sin(Math.toRadians(angle - 90)) + cy));

            points.add(point);
        }
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public Array<Star> getStars() {
        return stars;
    }

    public void addScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSE;
    }
}

