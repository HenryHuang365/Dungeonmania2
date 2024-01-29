package dungeonmania.entities.enemies;


import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.entities.enemies.enemymovement.SpiderMovement;
import dungeonmania.util.Position;


public class Spider extends Enemy {

    //private List<Position> movementTrajectory;
    //private int nextPositionElement;
    //private boolean forward;
    private Movement moveStrategy;

    public static final int DEFAULT_SPAWN_RATE = 0;
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_HEALTH = 10;

    public Spider(Position position, double health, double attack) {
        super(position.asLayer(Entity.DOOR_LAYER + 1), health, attack);
        /**
         * Establish spider movement trajectory Spider moves as follows:
         *  8 1 2       10/12  1/9  2/8
         *  7 S 3       11     S    3/7
         *  6 5 4       B      5    4/6
         */
        moveStrategy = new SpiderMovement(position.getAdjacentPositions(), 1, true);
        //movementTrajectory = position.getAdjacentPositions();
        //nextPositionElement = 1;
        //forward = true;
    };

    public void setMoveStrategy(Movement moveStrategy) {
        this.moveStrategy = moveStrategy;
    }
    /*
    private void updateNextPosition() {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
    }
    */

    @Override
    public void move(Game game) {
        /*
        Position nextPos = movementTrajectory.get(nextPositionElement);
        List<Entity> entities = game.getMap().getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        entities = game.getMap().getEntities(nextPos);
        if (entities == null
                || entities.size() == 0
                || entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), this))) {
            game.getMap().moveTo(this, nextPos);
            updateNextPosition();
        }
        */

        if (!ifStucked()) {
            moveStrategy.moveToByMovement(this, game);
        } else {
            getStuckedTile().tryToMoveAway(this);
        }
    }
}
