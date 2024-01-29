package dungeonmania.entities;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {


    public boolean isOkayToCollect() {
        //by default, collectable is good to collect, but there's always special item like bomb.
        //who has special pickup rule
         return true;
    }

    public CollectableEntity(Position position) {
        super(position);
    }

}
