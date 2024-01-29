package dungeonmania.entities.enemies.enemymovement;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SpiderMovement implements Movement {

    private List<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    public SpiderMovement(List<Position> movementTrajectory, int nextPositionElement, boolean forward) {
        this.movementTrajectory = movementTrajectory;
        this.nextPositionElement = nextPositionElement;
        this.forward = forward;
    }

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

    @Override
    public void moveToByMovement(Enemy enemy, Game game) {
        Position nextPos = movementTrajectory.get(nextPositionElement);
        GameMap map = game.getMap();
        // law of demeter
        List<Entity> entities = game.getMap().getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        // law of demeter - fixed
        // entities = game.getMap().getEntities(nextPos);
        entities = game.getEntities(nextPos);
        if (entities == null
                || entities.size() == 0
                || entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), enemy))) {
                map.moveTo(enemy, nextPos);
            updateNextPosition();
        }
    }
}
