package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.entities.enemies.enemymovement.RandomMove;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    private Movement moveStrategy = new RandomMove();

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }


    public void setMoveStrategy(Movement moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public void move(Game game) {
        /*
        Position nextPos;
        GameMap map = game.getMap();
        List<Position> pos = getPosition().getCardinallyAdjacentPositions();
        pos = pos
            .stream()
            .filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = getPosition();
            // law of demeter - fixed
            game.getMap().moveTo(this, nextPos);
        } else {
            // law of demeter - fixed
            nextPos = pos.get(randGen.nextInt(pos.size()));
            game.getMap().moveTo(this, nextPos);
        }
        */

        setMoveStrategy(new RandomMove());
        if (!ifStucked()) {
            moveStrategy.moveToByMovement(this, game);
        } else {
            getStuckedTile().tryToMoveAway(this);
        }
    }

}
