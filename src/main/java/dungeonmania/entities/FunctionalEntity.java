package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class FunctionalEntity extends Entity {

    public FunctionalEntity(Position position) {
        super(position);
    }
    public abstract void onOverlap(GameMap map, Entity entity);
    public abstract void onMovedAway(GameMap map, Entity entity);
}
