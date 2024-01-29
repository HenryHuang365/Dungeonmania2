package dungeonmania.entities.enemies.enemymovement;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMove implements Movement {

    private Random randGen = new Random();

    @Override
    public void moveToByMovement(Enemy e, Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        // law of demeter
        List<Position> pos = e.getCardinallyAdjacentPositions();
        pos = pos
            .stream()
            .filter(p -> map.canMoveTo(e, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            //stay in the same position
            nextPos = e.getPosition();
            // law of demeter - fixed
            // game.getMap().moveTo(e, nextPos);
            game.moveTo(e, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            // law of demeter - fixed
            // game.getMap().moveTo(e, nextPos);
            game.moveTo(e, nextPos);
        }
    }
}
