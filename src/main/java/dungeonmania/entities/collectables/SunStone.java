package dungeonmania.entities.collectables;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SunStone extends CollectableEntity implements InventoryItem {

    public SunStone(Position position) {
        super(position);
    }
    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
