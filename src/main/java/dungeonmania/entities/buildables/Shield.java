package dungeonmania.entities.buildables;


import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class Shield extends Buildable {
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            // law of demeter - fixed
            // game.getPlayer().remove(this);
            game.removeShield(this, game.getPlayer());
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            defence,
            1,
            1,
            origin.getPlayer(),
            true));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public static boolean isBuildable(int woodcount, int treasurecount, int keycount, int sunstonecount) {
        //check if we can build
        int targettreasureAndkeycount = 1;
        int avaliabletreasureAndkeycount = treasurecount + keycount;
        int avaliablesunstone = sunstonecount;

        //seperate sunstone
        while (avaliablesunstone != 0) {
            if (avaliabletreasureAndkeycount < targettreasureAndkeycount) {
                avaliabletreasureAndkeycount += 1;
                avaliablesunstone -= 1;
                continue;
            }
            break;
        }
        return woodcount >= 2 && avaliabletreasureAndkeycount >= targettreasureAndkeycount;
    }

    public static void build(List<InventoryItem> items, List<Wood> wood, List<Treasure> treasures, List<Key> keys) {
        //consume material
        items.remove(wood.get(0));
        items.remove(wood.get(1));

        if (treasures.size() >= 1) {
            items.remove(treasures.get(0));
        } else if (keys.size() >= 1) {
            items.remove(keys.get(0));
        } //else, there must be at least one sunstone


    }

}
