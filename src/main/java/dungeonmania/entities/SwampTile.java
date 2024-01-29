package dungeonmania.entities;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.entities.enemies.Enemy;
// import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends FunctionalEntity {

    public static final int DEFAULT_MOVEMENT_FACTOR = 2;
    private int moveMentFactor = DEFAULT_MOVEMENT_FACTOR;
    //hashmap which record the amount of tick the entities will be on
    private Map<Enemy, Integer> stuckedEntity = new HashMap<Enemy, Integer>();

    public SwampTile(Position position, int moveMentFactor) {
        super(position.asLayer(Entity.FLOOR_LAYER));
        //System.out.println(moveMentFactor);
        this.moveMentFactor = moveMentFactor;
    }

    public void getStuck(Enemy e) {
        stuckedEntity.put(e, moveMentFactor);
        e.setIfStucked(true);
        e.setStuckedTile(this);
    }

    /**
     * Enemy try to move away from the swamp by one tick
     * @param e the enemy
     */
    public void tryToMoveAway(Enemy e) {
        stuckedEntity.put(e, stuckedEntity.get(e) - 1);
        if (stuckedEntity.get(e) == 0) {
            e.setIfStucked(false);
            e.setStuckedTile(null);
        }

    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy) {
            if (((Enemy) entity).isAllied() || moveMentFactor == 0) return;
            getStuck((Enemy) entity);
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Enemy) {
            stuckedEntity.remove((Enemy) entity);
            ((Enemy) entity).setIfStucked(false);
            ((Enemy) entity).setStuckedTile(null);
        }
    }
}
