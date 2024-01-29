package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class OverlapReactEntity extends Entity {

    public OverlapReactEntity(Position position) {
        super(position);
    }

    public abstract void onOverlap(GameMap map, Entity entity);

}
