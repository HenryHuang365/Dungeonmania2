package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }


    public List<String> getBuildables(Game g) {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        int sunstones = count(SunStone.class);
        int swords = count(Sword.class);
        List<String> result = new ArrayList<>();

        if (Bow.isBuildable(wood, arrows)) {
            result.add("bow");
        }
        if (Shield.isBuildable(wood, treasure, keys, sunstones)) {
            result.add("shield");
        }
        if (MidnightArmour.isBuildable(swords, sunstones, g)) {
            result.add("midnight_armour");
        }
        // if (wood >= 2 && (treasure >= 1 || keys >= 1)) {
        //     result.add("shield");
        // }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, String entity, EntityFactory factory, Game g) {

        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);
        List<SunStone> sunstones = getEntities(SunStone.class);
        List<Sword> swords = getEntities(Sword.class);
        if (Bow.isBuildable(wood.size(), arrows.size()) && entity.equals("bow")) {
            if (remove) {
                // items.remove(wood.get(0));
                // items.remove(arrows.get(0));
                // items.remove(arrows.get(1));
                // items.remove(arrows.get(2));
                Bow.build(items, arrows, wood);
            }
            return factory.buildBow();

        } else if (
            Shield.isBuildable(wood.size(), treasure.size(), keys.size(), sunstones.size())
            && entity.equals("shield")
        ) {
            if (remove) {
                // items.remove(wood.get(0));
                // items.remove(wood.get(1));
                // if (treasure.size() >= 1) {
                //     items.remove(treasure.get(0));
                // } else {
                //     items.remove(keys.get(0));
                // }
                Shield.build(items, wood, treasure, keys);
            }
            return factory.buildShield();
        } else if (
            MidnightArmour.isBuildable(swords.size(), sunstones.size(), g)
            && entity.equals("midnight_armour")
        ) {
            if (remove) MidnightArmour.build(items, swords, sunstones);
            return factory.buildMidnightArmour();
        }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId)) return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public void weaponUse(Game game) {
        getWeapon().use(game);
    }

    public List<InventoryItem> getItems() {
        return items;
    }

}
