package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class DestryableEntity extends Entity  {
    public DestryableEntity(Position position) {
        super(position);
    }
    public abstract void onDestroy(GameMap map);
}
