package dungeonmania.entities;

import dungeonmania.util.Position;

/**
 * Entities that can not be destroyed/intract with
*/
public abstract class IntractableEntity extends Entity {

    public IntractableEntity(Position position) {
        super(position);
    }

}
