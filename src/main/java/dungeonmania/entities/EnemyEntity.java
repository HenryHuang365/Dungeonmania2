package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class EnemyEntity extends Entity {

    public EnemyEntity(Position position) {
        super(position);
    }
    public abstract void onOverlap(GameMap map, Entity entity);
    public abstract void onDestroy(GameMap map);

}
