package dungeonmania.entities.enemies.boss;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.entities.enemies.enemymovement.RandomMove;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Hydra extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    public static final double DEFAULT_HEALTH_INCREASE_RATE = 0.5;
    public static final double DEFAULT_HEALTH_INCREASE_AMOUNT = 2;
    private double healthIncreaseRate = Hydra.DEFAULT_HEALTH_INCREASE_RATE;
    private double healthincreaseamount = Hydra.DEFAULT_HEALTH_INCREASE_AMOUNT;
    private boolean isIncreaseHealth = false;

    private Movement moveStrategy = new RandomMove();

    public Hydra(
        Position position,
        double health,
        double attack,
        double healthIncreaseRate,
        double healthincreaseamount) {
        super(position, health, attack);
        this.healthIncreaseRate = healthIncreaseRate;
        this.healthincreaseamount = healthincreaseamount;
    }

    public void setMoveStrategy(Movement moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public void move(Game game) {
        setMoveStrategy(new RandomMove());
        if (!ifStucked()) {
            moveStrategy.moveToByMovement(this, game);
        } else {
            getStuckedTile().tryToMoveAway(this);
        }
    }

    public void randomIncreaseRate() {
        Random radomGenerator = new Random(System.currentTimeMillis());
        long bound = radomGenerator.nextInt(99) + 1; //randomly generate a number between 1 - 99
        long possibility = Math.round(healthIncreaseRate * 100);
        // System.out.println("possibility = " + possibility + " radint = " + bound);
        if (bound < possibility) {
            isIncreaseHealth = true;
        } else {
            isIncreaseHealth = false;
        }
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        randomIncreaseRate();
        super.onOverlap(map, entity);
    }

    public boolean isIncreaseHealth() {
        return isIncreaseHealth;
    }

    public double getHealthincreaseamount() {
        return healthincreaseamount;
    }

}
