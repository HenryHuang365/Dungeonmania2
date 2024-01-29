package dungeonmania.entities.enemies.enemymovement;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class FollowPlayerMove implements Movement {

    @Override
    public void moveToByMovement(Enemy e, Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        nextPos = map.dijkstraPathFind(e.getPosition(), map.getPlayerPosition(), e);
        map.moveTo(e, nextPos);
    }
}
